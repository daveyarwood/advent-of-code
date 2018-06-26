(ns advent.2015.12.03.puzzle-1
  (:require [clojure.java.io         :as    io]
            [clojure.spec.alpha      :as    s]
            [clojure.spec.test.alpha :as    stest]
            [advent.util.io          :refer (char-seq)]))

(s/def ::state (s/tuple ::coordinate ::present-counts))

(s/def ::x int?)
(s/def ::y int?)
(s/def ::coordinate (s/tuple ::x ::y))

(s/def ::present-counts (s/map-of ::coordinate int?))

(s/def ::instruction #{\^ \v \> \< \newline})

(s/fdef follow-instruction
        :args (s/cat :state ::state :instruction ::instruction)
        :ret ::state)

(defn follow-instruction
  [[[x y] counts] instruction]
  (let [coords (case instruction
                 \^ [x (inc y)]
                 \v [x (dec y)]
                 \> [(inc x) y]
                 \< [(dec x) y]
                 [x y])]
    [coords (update counts coords (fnil inc 0))]))

(s/fdef follow-instructions
        :args (s/cat :instructions (s/coll-of ::instruction :kind sequential?))
        :ret ::state)

(defn follow-instructions
  [instructions]
  (reduce follow-instruction [[0 0] {[0 0] 1}] instructions))

(stest/instrument)

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-03-01-input"))]
    (-> rdr char-seq follow-instructions second count prn)))

