(ns advent.2016.12.07.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-ip-parts
  [ip]
  (as-> ip ?
    (str/split ? #"\[|\]")
    [(take-nth 2 ?) (take-nth 2 (rest ?))]))

(defn contains-abba?
  [s]
  (->> s
       (partition 4 1)
       (some (fn [[a b c d]]
               (and (not= a b) (= [c d] [b a]))))))

(defn supports-tls?
  [ip]
  (let [[supernet-seqs hypernet-seqs] (parse-ip-parts ip)]
    (and (some contains-abba? supernet-seqs)
         (not-any? contains-abba? hypernet-seqs))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-07-01-input"))]
    (->> rdr line-seq (filter supports-tls?) count prn)))

