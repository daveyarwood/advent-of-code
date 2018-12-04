(ns advent.2017.12.12.puzzle-2
  (:require [advent.2017.12.12.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn program-groups
  [connections]
  (loop [groups #{}, [program & more] (keys connections)]
    (cond
      (not program)
      groups

      (some #(contains? % program) groups)
      (recur groups more)

      :else
      (recur (conj groups (p1/program-group program connections)) more))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-12-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-connections)
         (into {})
         program-groups
         count
         prn)))
