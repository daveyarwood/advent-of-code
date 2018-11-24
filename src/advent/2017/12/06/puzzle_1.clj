(ns advent.2017.12.06.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-input
  [s]
  (as-> s ?
    (str/trim ?)
    (str/split ? #"\t")
    (mapv #(Integer/parseInt %) ?)))

(defn reallocate
  [banks]
  (let [[i blocks] (->> banks
                        (map-indexed vector)
                        ;; When there is a tie, max-key chooses the last one,
                        ;; but for this problem, we need to choose the first
                        ;; one. So, we reverse the list first.
                        reverse
                        (apply max-key second))
        next-bank  #(rem (inc %) (count banks))
        banks      (-> banks vec (assoc i 0))
        i          (next-bank i)]
    (loop [i i, blocks blocks, banks banks]
      (if (zero? blocks)
        banks
        (recur (next-bank i) (dec blocks) (update banks i inc))))))

(defn reallocation-cycles
  [banks]
  (iterate reallocate banks))

(defn take-while-unique
  "Returns a sequence up until (and NOT including) the first element that
   already occurred earlier in the sequence."
  [input-seq]
  (loop [return-seq [], [x & more] input-seq]
    (if ((set return-seq) x)
      (seq return-seq)
      (recur (concat return-seq [x]) more))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-06-01-input"))]
    (-> rdr
        slurp
        parse-input
        reallocation-cycles
        take-while-unique
        count
        prn)))
