(ns advent.2015.12.13.puzzle-1
  (:require [clojure.math.combinatorics :as combo]
            [clojure.java.io            :as io]))

(def sentence-pattern
  #"(\w+) would (\w+) (\d+) happiness units by sitting next to (\w+).")

(defn parse-affinities
  [sentences]
  (reduce (fn [affinities sentence]
            (as-> sentence ?
              (re-matches sentence-pattern ?)
              (let [[_ person-1 verb units person-2] ?
                    change (* (Integer/parseInt units)
                              (case verb
                                "gain" 1
                                "lose" -1
                                (throw (ex-info "invalid verb" {:verb verb}))))]
                (assoc-in affinities [person-1 person-2] change))))
          {}
          sentences))

(defn seating-arrangements
  [affinities]
  (for [order (->> affinities keys combo/permutations (map vec))
        :let [edges (->> (conj order (first order))
                         (partition 2 1))]]
    [order (reduce (fn [total-change [person-1 person-2]]
                     (+ total-change
                        (get-in affinities [person-1 person-2])
                        (get-in affinities [person-2 person-1])))
                   0
                   edges)]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-13-01-input"))]
    (->> rdr
         line-seq
         parse-affinities
         seating-arrangements
         (apply max-key second)
         second
         prn)))
