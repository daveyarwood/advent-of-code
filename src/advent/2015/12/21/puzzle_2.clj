(ns advent.2015.12.21.puzzle-2
  (:require [advent.2015.12.21.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [items-rdr (io/reader (io/resource "2015-12-21-01-items"))
              stats-rdr (io/reader (io/resource "2015-12-21-01-stats"))]
    (let [items (-> items-rdr slurp p1/parse-items)
          boss  (-> stats-rdr slurp p1/parse-stats)]
      (->> items
           p1/possible-items
           (filter (partial (complement p1/player-will-win?) p1/player boss))
           (apply max-key p1/total-cost)
           p1/total-cost
           prn))))

