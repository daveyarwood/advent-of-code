(ns advent.2016.12.21.puzzle-2
  (:require [advent.util.seq            :as    seq]
            [advent.2016.12.21.puzzle-1 :as    p1]
            [clojure.core.match         :refer [match]]
            [clojure.java.io            :as    io]
            [clojure.string             :as    str]))

(defn follow-instruction-in-reverse
  [output instruction]
  (match instruction
    [:swap-positions x y]
    (-> output
        (p1/update-chars assoc x (get output y))
        (p1/update-chars assoc y (get output x)))

    [:swap-letters x y]
    (str/replace output (re-pattern (str x \| y)) {x y, y x})

    [:rotate-direction direction steps]
    (let [n (case direction :right steps, :left (- steps))]
      (p1/update-chars output (partial seq/rotate n)))

    [:rotate-based-on-position letter]
    (p1/update-chars output
                     #(->> %
                           (iterate (partial seq/rotate 1))
                           (take (count %))
                           (filter (fn [rotation]
                                     (as-> rotation ?
                                       (apply str ?)
                                       (p1/rotate-based-on-position ? letter)
                                       (= output ?))))
                           first))

    [:reverse-segment x y]
    (p1/update-chars output #(let [segment (subvec % x (inc y))]
                               (concat (take x %)
                                       (reverse segment)
                                       (drop (+ x (count segment)) %))))

    [:move-position x y]
    (p1/update-chars output #(let [letter-at-y      (nth % y)
                                   output-without-y (concat (take y %)
                                                            (drop (inc y) %))]
                               (concat (take x output-without-y)
                                       [letter-at-y]
                                       (drop x output-without-y))))))

(defn follow-instructions-in-reverse
  [output instructions]
  (reduce follow-instruction-in-reverse output (reverse instructions)))

(def output "fbgdceah")

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-21-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-instruction)
         (follow-instructions-in-reverse output)
         println)))
