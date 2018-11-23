(ns advent.2017.12.04.puzzle-2
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn valid-passphrase?
  [s]
  (as-> s ?
    (str/split ? #"\s+")
    (map sort ?)
    (frequencies ?)
    (not-any? #(> (val %) 1) ?)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-04-01-input"))]
    (->> rdr line-seq (filter valid-passphrase?) count prn)))
