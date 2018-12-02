(ns advent.2017.12.10.puzzle-2
  (:require [advent.2017.12.10.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.string             :as str]))

(defn input-lengths
  [s]
  (as-> s ?
    (map int ?)
    (concat ? [17 31 73 47 23])))

(defn run-algo-times
  [lengths times]
  (->> [(vec (range 256)) 0 0]
       (iterate #(p1/knot-hash lengths %))
       (drop times)
       first
       first))

(defn dense-hash
  [sparse-hash]
  (->> sparse-hash
       (partition 16)
       (map (partial apply bit-xor))))

(defn hex-string
  [dense-hash]
  (->> dense-hash
       (map #(let [hex (Integer/toString % 16)]
               (if (= 2 (count hex))
                 hex
                 (str \0 hex))))
       (apply str)))

(defn knot-hash
  [s]
  (-> s
      input-lengths
      (run-algo-times 64)
      dense-hash
      hex-string))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-10-01-input"))]
    (-> rdr slurp str/trim knot-hash prn)))

