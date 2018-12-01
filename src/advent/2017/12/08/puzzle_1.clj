(ns advent.2017.12.08.puzzle-1
  (:require [clojure.java.io :as io]))

(defn parse-instruction
  [s]
  (let [[_ reg op arg cond-reg cond-op cond-arg]
        (re-matches
          #"(\w+)\s+(\w+)\s+(-?\d+)\s+if\s+(\w+)\s+([^\s]+)\s+(-?\d+)"
          s)]
    {:operation {:register  reg
                 :operator  op
                 :arg       (Integer/parseInt arg)}
     :condition {:register  cond-reg
                 :operator  cond-op
                 :arg       (Integer/parseInt cond-arg)}}))

(defn check-condition
  [{:keys [register operator arg]} registers]
  (let [register-value (get registers register 0)
        op-fn          (case operator
                         ">"  >
                         "<"  <
                         ">=" >=
                         "<=" <=
                         "==" =
                         "!=" not=)]
    (op-fn register-value arg)))

(defn apply-operation
  [{:keys [register operator arg]} registers]
  (let [op-fn (case operator
                "inc" +
                "dec" -)]
    (update registers register (fnil #(op-fn % arg) 0))))

(defn execute-instruction
  [registers {:keys [operation condition]}]
  (if (check-condition condition registers)
    (apply-operation operation registers)
    registers))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-08-01-input"))]
    (->> rdr
         line-seq
         (map parse-instruction)
         (reduce execute-instruction {})
         vals
         (apply max 0)
         prn)))
