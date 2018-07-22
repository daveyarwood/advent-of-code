(ns advent.2016.12.04.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-room
  [line]
  (as-> line ?
    (re-matches #"([a-z-]+)-(\d+)\[([a-z]{5})\]" ?)
    (rest ?)
    (let [[encrypted-name sector-id checksum] ?]
      [encrypted-name (Integer/parseInt sector-id) checksum])))

(defn remove-dashes
  [s]
  (str/replace s #"-" ""))

(defn real?
  [[encrypted-name sector-id checksum :as room]]
  (->> encrypted-name
       remove-dashes
       frequencies
       (group-by val)
       sort
       reverse
       (mapcat (fn [[_ letter-freqs]]
                 (->> letter-freqs
                      (map (fn [[letter freq]] letter))
                      sort)))
       (take 5)
       (apply str)
       (= checksum)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-04-01-input"))]
    (->> rdr
         line-seq
         (map parse-room)
         (filter real?)
         (map second)
         (apply +)
         prn)))
