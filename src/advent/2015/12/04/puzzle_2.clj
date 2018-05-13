(ns advent.2015.12.04.puzzle-2
  (:require [advent.2015.12.04.puzzle-1 :as p1]))

(def difficulty 6)

(defn -main
  []
  (prn (p1/mine difficulty p1/secret-key)))
