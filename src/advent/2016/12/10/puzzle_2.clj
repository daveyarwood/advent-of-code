(ns advent.2016.12.10.puzzle-2
  (:require [advent.2016.12.10.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn future-awaiting-output
  [n]
  (future
    (loop []
      (if-let [[value] (get @p1/state [:output n])]
        (do (prn :output n :value value) value)
        (do (Thread/sleep 500) (recur))))))

(def output-0 (future-awaiting-output 0))
(def output-1 (future-awaiting-output 1))
(def output-2 (future-awaiting-output 2))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-10-01-input"))]
    (println "Spawning bots...")
    (->> rdr line-seq (map p1/parse-instruction) p1/spawn-bots!)
    (println "Waiting for the solution...")
    (let [[o0 o1 o2] (map deref [output-0 output-1 output-2])]
      (prn :product (* o0 o1 o2)))))
