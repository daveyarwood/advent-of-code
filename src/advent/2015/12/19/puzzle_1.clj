(ns advent.2015.12.19.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.set     :as set]
            [clojure.string  :as str]))

(defn parse-replacement
  [line]
  (rest (re-matches #"(\w+) => (\w+)" line)))

(defn possible-replacements
  [molecule [from to]]
  (loop [results     #{}
         start-index 0]
    (if-let [index (str/index-of molecule from start-index)]
      (recur (conj results
                   (apply str
                          (subs molecule 0 index)
                          to
                          (subs molecule (+ index (count from)))))
             (inc index))
      results)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-19-01-input"))]
    (let [lines        (line-seq rdr)
          molecule     (last lines)
          replacements (->> lines (take-while seq) (map parse-replacement))]
      (->> replacements
           (map (partial possible-replacements molecule))
           (apply set/union)
           count
           prn))))
