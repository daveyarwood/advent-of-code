(ns advent.2015.12.07.puzzle-1
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn parse-connection
  [circuit input-line]
  (let [args (for [arg (str/split input-line #" ")]
               (if (re-matches #"-?\d+" arg)
                 (Integer/parseInt arg)
                 arg))]
    (case (count args)
      ;; signal provided to output
      3 (let [[input _ output] args]
          (assoc circuit output [identity [input]]))
      ;; unary operation on input provided to output
      4 (let [[op input _ output] args]
          (assert (= "NOT" op) "The only valid unary operation is NOT.")
          (assoc circuit output [bit-not [input]]))
      ;; binary operation on two things provided to output
      5 (let [[input1 op input2 _ output] args
              f (case op
                  "AND"    bit-and
                  "OR"     bit-or
                  "LSHIFT" bit-shift-left
                  "RSHIFT" bit-shift-right)]
          (assoc circuit output [f [input1 input2]])))))

(defn parse-circuit
  [input-lines]
  (reduce parse-connection {} input-lines))

(defn run-circuit
  [circuit]
  (let [wires (zipmap (keys circuit) (repeatedly promise))]
    (doseq [[output [thunk inputs]] circuit]
      (future
        (->> inputs
             (map #(if (string? %) @(get wires %) %))
             doall
             (apply thunk)
             (deliver (get wires output)))))
    wires))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-07-01-input"))]
    (-> rdr line-seq parse-circuit run-circuit (get "a") deref prn)))

