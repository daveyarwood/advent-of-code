(ns advent.2016.12.14.puzzle-1
  (:require [clojure.string :as str]
            [digest]))

(def puzzle-input "zpqevtbw")

(defn hashes
  [salt]
  (map #(digest/md5 (str salt %)) (range)))

(defn first-triplet-char
  [s]
  (some (fn [[a b c]] (when (= a b c) a))
        (partition 3 1 s)))

(defn key?
  [[hash & next-hashes]]
  (when-let [triplet-char (first-triplet-char hash)]
    (some #(str/includes? % (apply str (repeat 5 triplet-char))) next-hashes)))

(defn -main
  []
  (->> puzzle-input
       hashes
       (partition 1001 1)
       (map-indexed #(vector %1 (key? %2)))
       (filter second)
       (map first)
       (drop 63)
       first
       prn))
