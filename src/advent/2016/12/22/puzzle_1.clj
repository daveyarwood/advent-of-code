(ns advent.2016.12.22.puzzle-1
  (:require [clojure.java.io            :as io]
            [clojure.math.combinatorics :as combo]
            [clojure.string             :as str]))

(defn- parse-terabytes
  [s]
  (->> s
       (re-matches #"(\d+)T")
       rest
       first
       (#(Integer/parseInt %))))

(defn parse-node
  [line]
  (let [[address size used available _] (str/split line #"\s+")
        [x y] (->> address
                   (re-matches #"/dev/grid/node-x(\d+)-y(\d+)")
                   rest
                   (map #(Integer/parseInt %)))]
    {:position  [x y]
     :size      (parse-terabytes size)
     :used      (parse-terabytes used)
     :available (parse-terabytes available)}))

(defn all-pairs
  [nodes]
  (let [combos         (combo/combinations nodes 2)
        reverse-combos (map reverse combos)]
    (concat combos reverse-combos)))

(defn viable?
  [[node-a node-b]]
  (and (pos? (:used node-a))
       ;; This should always be true, but just in case...
       (not= (:position node-a) (:position node-b))
       (<= (:used node-a) (:available node-b))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-22-01-input"))]
    (->> rdr
         line-seq
         (drop 2)
         (map parse-node)
         all-pairs
         (filter viable?)
         count
         prn)))
