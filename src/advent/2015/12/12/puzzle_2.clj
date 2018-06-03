(ns advent.2015.12.12.puzzle-2
  (:require [clojure.data.json :as json]
            [clojure.java.io   :as io]))

(defprotocol INumericValueV2
  (numeric-value-v2 [this]))

(extend-protocol INumericValueV2
  clojure.lang.Sequential
  (numeric-value-v2 [this] (->> this (map numeric-value-v2) (reduce +)))

  clojure.lang.IPersistentMap
  (numeric-value-v2 [this]
    (if (some #{"red"} (vals this))
      0
      (->> this vals (map numeric-value-v2) (reduce +))))

  String
  (numeric-value-v2 [this] 0)

  Number
  (numeric-value-v2 [this] this))

(defn -main []
  (with-open [rdr (io/reader (io/resource "2015-12-12-01-input.json"))]
    (->> rdr json/read numeric-value-v2 prn)))
