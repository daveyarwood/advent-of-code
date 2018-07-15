(ns advent.2016.12.02.puzzle-2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn base-14-char-fn
  "Given a fn that takes and returns an integer 0-13, returns a fn that does the
   same thing, but takes and returns a character (0-9 then A-D)."
  [f]
  #(-> % (Character/digit 14) f (Character/forDigit 14) Character/toUpperCase))

(def right (base-14-char-fn inc))
(def left (base-14-char-fn dec))

(defn up
  [c]
  (case c
    \3 \1
    \D \B
    ((base-14-char-fn #(- % 4)) c)))

(defn down
  [c]
  (case c
    \1 \3
    \B \D
    ((base-14-char-fn #(+ % 4)) c)))

(defn follow-instruction
  [button c]
  (case c
    \R (if (#{\1 \4 \9 \C \D} button) button (right button))
    \L (if (#{\1 \2 \5 \A \D} button) button (left button))
    \U (if (#{\5 \2 \1 \4 \9} button) button (up button))
    \D (if (#{\5 \A \D \C \9} button) button (down button))
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
         (reduce follow-instructions [[] \5])
         first
         str/join
         prn)))
