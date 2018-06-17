(ns advent.2015.12.16.puzzle-1
  (:require [clojure.java.io :as io]))

(defn parse-traces
  [lines]
  (reduce (fn [m line]
            (let [[_ material amount] (re-matches #"(\w+): (\d+)" line)]
              (assoc m material (Integer/parseInt amount))))
          {}
          lines))

(defn parse-sue-facts
  [line]
  (let [sue-number (->> line
                        (re-matches #"Sue (\d+).*")
                        second
                        Integer/parseInt)
        facts      (->> (re-seq #"([A-Za-z]+): (\d+)" line)
                        (reduce (fn [m [_ thing amount]]
                                  (assoc m thing (Integer/parseInt amount)))
                                {}))]
    [sue-number facts]))

(defn -main
  []
  (with-open [gift-rdr (io/reader (io/resource "2015-12-16-01-gift"))
              sues-rdr (io/reader (io/resource "2015-12-16-01-sues"))]
    (let [gift-traces (->> gift-rdr line-seq parse-traces)
          sue-facts   (->> sues-rdr line-seq (map parse-sue-facts))]
      (->> sue-facts
           (filter (fn [[_ facts]]
                     (every? (fn [[k v]] (= v (get gift-traces k))) facts)))
           prn))))
