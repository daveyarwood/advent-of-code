(ns advent.2016.12.14.puzzle-2
  (:require [advent.2016.12.14.puzzle-1 :as p1]
            [digest]))

(defn stretch-hash
  [hash]
  (->> hash
       (iterate digest/md5)
       (drop 2016)
       first))

(defn -main
  []
  (->> p1/puzzle-input
       p1/hashes
       (map stretch-hash)
       (partition 1001 1)
       (map-indexed #(vector %1 (p1/key? %2)))
       (filter second)
       (map first)
       (drop 63)
       first
       prn))
