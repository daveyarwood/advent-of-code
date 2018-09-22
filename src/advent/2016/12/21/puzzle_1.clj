(ns advent.2016.12.21.puzzle-1
  (:require [advent.util.seq    :as    seq]
            [clojure.core.match :refer [match]]
            [clojure.java.io    :as    io]
            [clojure.string     :as    str]))

(defn instruction-parser
  [op regex matches-transform-fn]
  (fn [line]
    (let [matches (some->> line (re-matches regex) rest)]
      (when matches
        (apply vector op (matches-transform-fn matches))))))

(def parse-swap-positions
  (instruction-parser
    :swap-positions
    #"swap position (\d+) with position (\d+)"
    (partial map #(Integer/parseInt %))))

(def parse-swap-letters
  (instruction-parser
    :swap-letters
    #"swap letter ([a-z]) with letter ([a-z])"
    identity))

(def parse-rotate-direction
  (instruction-parser
    :rotate-direction
    #"rotate (right|left) (\d+) steps?"
    (fn [[direction steps]]
      [(keyword direction) (Integer/parseInt steps)])))

(def parse-rotate-based-on-position
  (instruction-parser
    :rotate-based-on-position
    #"rotate based on position of letter ([a-z])"
    identity))

(def parse-reverse-segment
  (instruction-parser
    :reverse-segment
    #"reverse positions (\d+) through (\d+)"
    (partial map #(Integer/parseInt %))))

(def parse-move-position
  (instruction-parser
    :move-position
    #"move position (\d+) to position (\d+)"
    (partial map #(Integer/parseInt %))))

(defn parse-instruction
  [line]
  (or (parse-swap-positions line)
      (parse-swap-letters line)
      (parse-rotate-direction line)
      (parse-rotate-based-on-position line)
      (parse-reverse-segment line)
      (parse-move-position line)
      (throw (ex-info "Failed to parse instruction"
                      {:line line}))))

(defn update-chars
  "Calls `f` on the characters in a string, which is provided to `f` as a vector
   for convenience. Optionally provides additional `args` to `f`.

   Returns the modified characters as a string."
  [s f & args]
  (as-> s ?
    (vec ?)
    (apply f ? args)
    (apply str ?)))

;; Extracting this one in particular out into a separate function
;; because part of puzzle 2 relies on it.
(defn rotate-based-on-position
  [input letter]
  (let [i (str/index-of input letter)
        n (+ 1 i (if (>= i 4) 1 0))]
    (update-chars input (partial seq/rotate (- n)))))

(defn follow-instruction
  [input instruction]
  (match instruction
    [:swap-positions x y]
    (-> input
        (update-chars assoc x (get input y))
        (update-chars assoc y (get input x)))

    [:swap-letters x y]
    (str/replace input (re-pattern (str x \| y)) {x y, y x})

    [:rotate-direction direction steps]
    (let [n (case direction :right (- steps), :left steps)]
      (update-chars input (partial seq/rotate n)))

    [:rotate-based-on-position letter]
    (rotate-based-on-position input letter)

    [:reverse-segment x y]
    (update-chars input #(let [segment (subvec % x (inc y))]
                           (concat (take x %)
                                   (reverse segment)
                                   (drop (+ x (count segment)) %))))

    [:move-position x y]
    (update-chars input #(let [letter-at-x     (nth % x)
                               input-without-x (concat (take x %)
                                                       (drop (inc x) %))]
                           (concat (take y input-without-x)
                                   [letter-at-x]
                                   (drop y input-without-x))))))

(defn follow-instructions
  [input instructions]
  (reduce follow-instruction input instructions))

(def input "abcdefgh")

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-21-01-input"))]
    (->> rdr
         line-seq
         (map parse-instruction)
         (follow-instructions input)
         println)))
