(ns advent.2016.12.20.puzzle-2
  (:require [advent.2016.12.20.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(def max-value 4294967295)

(defn blacklisted?
  [ranges n]
  (some (fn [[lower-bound upper-bound]] (<= lower-bound n upper-bound))
        ranges))

(defn next-non-blacklisted-number
  [ranges cursor]
  (loop [previous-result nil]
    (let [result (reduce (fn [n [lower-bound upper-bound]]
                           (if (<= lower-bound n upper-bound)
                             (inc upper-bound)
                             n))
                         (or previous-result (inc cursor))
                         ranges)]
      (cond
        (not= previous-result result)
        (recur result)

        (> result max-value)
        nil

        :else
        result))))

(defn next-blacklisted-number
  [ranges cursor]
  (let [next-lower-bound (->> ranges
                              (map first)
                              sort
                              (drop-while #(<= % cursor))
                              first)]
    (cond
      ;; If we happen to be at `max-value` already, then there are no higher
      ;; numbers, period, therefore there can be no next blacklisted number, so
      ;; we return nil.
      (<= max-value cursor)
      nil

      ;; If the next number above `cursor` is in a blacklisted range, then
      ;; that's obviously the next blacklisted number after `cursor`.
      (some (fn [[lower-bound upper-bound]]
              (<= lower-bound (inc cursor) upper-bound))
            ranges)
      (inc cursor)

      ;; If the next number above `cursor` is NOT in a blacklisted range, then
      ;; the next number that IS in a blacklisted range will be the lower bound
      ;; of that range.
      next-lower-bound
      next-lower-bound

      ;; If we've gotten this far, there is no blacklisted range above the
      ;; cursor, so we return nil.
      :else
      nil)))

(defn count-numbers-not-in-a-range
  [ranges]
  (loop [sum 0, cursor 0]
    (cond
      (>= cursor max-value)
      sum

      (blacklisted? ranges cursor)
      (if-let [next-number (next-non-blacklisted-number ranges cursor)]
        (recur sum next-number)
        sum)

      :else
      (if-let [ceiling (next-blacklisted-number ranges cursor)]
        (recur (+ sum (- ceiling cursor)) ceiling)
        (+ sum (- (inc max-value) cursor))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-20-01-input"))]
    (->> rdr line-seq (map p1/parse-range) count-numbers-not-in-a-range prn)))
