(ns advent.2015.12.12.puzzle-1
  (:require [clojure.data.json :as json]
            [clojure.java.io   :as io]))

(defprotocol INumericValue
  (numeric-value [this]))

(extend-protocol INumericValue
  clojure.lang.Sequential
  (numeric-value [this] (->> this (map numeric-value) (reduce +)))

  clojure.lang.IPersistentMap
  (numeric-value [this] (->> this vals (map numeric-value) (reduce +)))

  String
  (numeric-value [this] 0)

  Number
  (numeric-value [this] this))

(defn -main []
  (with-open [rdr (io/reader (io/resource "2015-12-12-01-input.json"))]
    (->> rdr json/read numeric-value prn)))
