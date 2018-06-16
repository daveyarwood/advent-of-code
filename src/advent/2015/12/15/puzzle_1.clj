(ns advent.2015.12.15.puzzle-1
  (:require [clojure.java.io :as io]))

(defn parse-ingredient
  [s]
  (let [ingredient (second (re-matches #"^(\w+):.*" s))
        properties (->> (re-seq #"(\w+) (-?\d+)" s)
                        (reduce (fn [props [_ prop value]]
                                  (assoc props prop (Integer/parseInt value)))
                                {}))]
    [ingredient properties]))

(def
  ^{:doc
    "Returns all the different permutations of `n` integers that add up to
     `total`."}
  proportions
  (memoize
    (fn proportions
     [n total]
     (case n
       0 ()
       1 (list (list total))
       (->> (range (inc total))
            (map #(map (fn [props] (cons % props))
                       (proportions (dec n) (- total %))))
            (apply concat))))))

(defn score-and-calories
  [ingredients proportions]
  (let [property-totals (for [property (-> ingredients first second keys)]
                          (->> proportions
                               (map (fn [[_ properties] proportion]
                                      (* proportion
                                         (get properties property)))
                                    ingredients)
                               (reduce +)
                               (max 0)
                               (vector property)))
        total-calories  (->> property-totals
                             (filter #(= "calories" (first %)))
                             first
                             second)
        score           (->> property-totals
                             (remove #(= "calories" (first %)))
                             (map second)
                             (reduce *))]
    [score total-calories]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-15-01-input"))]
    (let [ingredients (->> rdr line-seq (map parse-ingredient))]
      (->> (for [proportions (proportions (count ingredients) 100)]
             (score-and-calories ingredients proportions))
           (map first)
           (apply max)
           prn))))

