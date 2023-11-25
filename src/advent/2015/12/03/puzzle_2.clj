(ns advent.2015.12.03.puzzle-2
  (:require [clojure.java.io            :as    io]
            [advent.2015.12.03.puzzle-1 :as    p1]
            [advent.io                  :refer [char-seq]]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-03-01-input"))]
    (let [instructions            (char-seq rdr)
          santa-instructions      (->> instructions (take-nth 2))
          robo-santa-instructions (->> instructions rest (take-nth 2))]
      (->> [santa-instructions robo-santa-instructions]
           (pmap (comp second p1/follow-instructions))
           (apply merge-with +)
           count
           prn))))
