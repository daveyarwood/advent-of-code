(ns advent.2015.12.14.puzzle-1
  (:require [clojure.java.io :as io]))

;; This solution seems to be correct up to 1000 iterations, but is incorrect as
;; of 2503 iterations. I have no idea why, and digging into it was frustrating
;; enough that I gave up.
;;
;; I also punted on puzzle 2, though it would have been easy to implement once I
;; had puzzle 1 producing correct answers. Solving puzzle 2 seemed silly given
;; that the answer would have been wrong (building on top of my incorrect puzzle
;; 1 solution).
;;
;; Oh well. Moving on.

(def sentence-pattern
  (re-pattern (str "(\\w+) can fly (\\d+) km/s for (\\d+) seconds, "
                   "but then must rest for (\\d+) seconds.")))

(defn parse-reindeer
  [sentence]
  (let [[_ name speed fly-time rest-time]
        (re-matches sentence-pattern sentence)

        [speed fly-time rest-time]
        (map #(Integer/parseInt %) [speed fly-time rest-time])]
    {:name      name
     :speed     speed
     :fly-time  fly-time
     :rest-time rest-time
     :traveled  0
     :state     :flying
     :timer     fly-time}))

(defn elapse-second
  [{:keys [speed fly-time rest-time traveled state timer] :as reindeer}]
  (let [[state timer traveled]
        (if (pos? timer)
          [state (dec timer) (if (= :flying state)
                               (+ traveled speed)
                               traveled)]
          (if (= :flying state)
            [:resting (dec rest-time) traveled]
            [:flying fly-time traveled]))]
    (assoc reindeer
           :state    state
           :timer    timer
           :traveled traveled)))

(defn elapse-seconds
  [n]
  (fn [reindeer]
    (->> reindeer (iterate elapse-second) (drop n) first)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-14-01-input"))]
    (->> rdr
         line-seq
         (map (comp (elapse-seconds 2503) parse-reindeer))
         (apply max-key :traveled)
         prn)))

