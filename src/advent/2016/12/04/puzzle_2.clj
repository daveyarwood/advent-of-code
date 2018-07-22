(ns advent.2016.12.04.puzzle-2
  (:require [advent.2016.12.04.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.string             :as str]))

(def ALPHABET "abcdefghijklmnopqrstuvwxyz")

(defn shift
  [amount character]
  (if ((set ALPHABET) character)
    (->> character (str/index-of ALPHABET) (+ amount) (nth (cycle ALPHABET)))
    character))

(defn decrypt-name
  [[encrypted-name sector-id checksum]]
  (let [decrypted-name (->> encrypted-name
                            (map (partial shift sector-id))
                            (apply str))]
    [decrypted-name sector-id checksum]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-04-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-room)
         (filter p1/real?)
         (map decrypt-name)
         (filter (fn [[name sector-id checksum]] (re-find #"pole" name)))
         prn)))
