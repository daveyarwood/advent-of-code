(ns advent.2016.12.07.puzzle-2
  (:require [advent.2016.12.07.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.string             :as str]))

(defn abas
  [s]
  (->> s
       (partition 3 1)
       (filter (fn [[a b c]]
                 (and (not= a b) (= a c))))))

(defn contains-bab?
  [s [a b _ :as aba]]
  (->> s
       (partition 3 1)
       (some (partial = [b a b]))))

(defn supports-ssl?
  [ip]
  (let [[supernet-seqs hypernet-seqs] (p1/parse-ip-parts ip)
        abas                          (mapcat abas supernet-seqs)]
    (some #(some (partial contains-bab? %)
                 abas)
          hypernet-seqs)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-07-01-input"))]
    (->> rdr line-seq (filter supports-ssl?) count prn)))

