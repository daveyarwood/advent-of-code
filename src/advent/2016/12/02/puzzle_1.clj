(ns advent.2016.12.02.puzzle-1
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn follow-instruction
  [button c]
  (case c
    \R (if (#{3 6 9} button) button (inc button))
    \L (if (#{1 4 7} button) button (dec button))
    \U (if (#{1 2 3} button) button (- button 3))
    \D (if (#{7 8 9} button) button (+ button 3))
    (throw (ex-info "Unrecognized character" {:character c}))))

(defn follow-instructions
  [[code-digits current-button] line]
  (let [button (reduce follow-instruction current-button line)]
    [(conj code-digits button) button]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-02-01-input"))]
    (->> rdr
         line-seq
         (reduce follow-instructions [[] 5])
         first
         str/join
         prn)))
