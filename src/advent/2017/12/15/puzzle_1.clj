(ns advent.2017.12.15.puzzle-1
  (:require [advent.math :as math]))

;;; Generic generator machinery, used for both puzzles 1 and 2

(defn- generator-impl
  [value generate-value-fn]
  (lazy-seq
    (cons value
          (generator-impl (generate-value-fn value) generate-value-fn))))

(defn generator
  "Given a factor and a starting value, returns a lazy sequence of the values
   produced by the generator described in the problem."
  [starting-value generate-value-fn]
  ;; Drops the starting value, as it doesn't count as a generated value.
  (drop 1 (generator-impl starting-value generate-value-fn)))

(defn pair-generator
  "Given 2 generators, returns a lazy sequence of pairs of the value produced by
   each generator."
  [gen1 gen2]
  (lazy-seq
    (cons [(first gen1) (first gen2)]
          (pair-generator (rest gen1) (rest gen2)))))

;;; Judging function

(defn last-16-bits-match?
  [n1 n2]
  (= (math/last-n-bits 16 n1)
     (math/last-n-bits 16 n2)))

;;; Generator logic for puzzle 1

(defn p1-generator
  "Returns a generator that starts at (but does not produce) `starting-value`
   and produces values obtained by multiplying the previous value by `factor`,
   dividing by 2147483647, and returning the remainder."
  [factor starting-value]
  (generator starting-value (fn [value]
                              (-> value
                                  (* factor)
                                  (rem 2147483647)))))

(comment
  ;; false
  (last-16-bits-match? 1092455 430625591)
  ;; true
  (last-16-bits-match? 245556042 1431495498)

  ;; Example Generator A
  (take 5 (p1-generator 16807 65))
  ;; Example Generator B
  (take 5 (p1-generator 48271 8921))

  ;; How long does it take Generator A to produce its 40 millionth value?
  ;; ~1.6 seconds
  (time (nth (p1-generator 16807 65) 40000000))

  (let [pairs (pair-generator
                (p1-generator 16807 65)
                (p1-generator 48271 8921))]
    (->> (take 5 pairs)
         (map (fn [[n1 n2]] (last-16-bits-match? n1 n2))))))

(defn -main
  []
  (let [gen-a (p1-generator 16807 703)
        gen-b (p1-generator 48271 516)]
    (->> (pair-generator gen-a gen-b)
         (take 40000000)
         (reduce (fn [tally [n1 n2]]
                   (if (last-16-bits-match? n1 n2)
                     (inc tally)
                     tally))
                 0)
         prn)))
