(ns advent.2017.12.09.puzzle-2
  (:require [advent.2017.12.09.puzzle-1 :as p1]
            [advent.util.io             :as util]
            [clojure.java.io            :as io]))

(defn count-garbage-chars
  [tokens]
  (->> (for [[token-type _ garbage-chars] tokens
             :when (= :garbage token-type)]
         (count garbage-chars))
       (apply +)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-09-01-input"))]
    (->> rdr util/char-seq p1/tokenize count-garbage-chars prn)))
