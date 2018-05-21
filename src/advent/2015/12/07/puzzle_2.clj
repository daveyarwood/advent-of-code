(ns advent.2015.12.07.puzzle-2
  (:require [advent.2015.12.07.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.string             :as str]))

(defn -main
  []
  (let [p1-result (-> (p1/-main) with-out-str str/trim Integer/parseInt)]
    (with-open [rdr (io/reader (io/resource "2015-12-07-01-input"))]
      (-> rdr
          line-seq
          p1/parse-circuit
          (assoc "b" [identity [p1-result]])
          p1/run-circuit
          (get "a")
          deref
          prn))))
