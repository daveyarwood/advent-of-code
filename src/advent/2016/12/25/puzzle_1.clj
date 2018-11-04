(ns advent.2016.12.25.puzzle-1
  (:require [advent.2016.12.12.puzzle-1 :as prev]
            [clojure.core.match         :refer (match)]
            [clojure.java.io            :as io]))

(def sample-size 1000)

(def expected (cycle [0 1]))

(defn run-instructions
  [initial-state instructions]
  (loop [i            0
         registers    initial-state
         instructions instructions
         transmission []]
    (if (>= i (count instructions))
      registers
      (let [[instruction & args] (nth instructions i)]
        (case instruction
          "cpy" (recur (inc i)
                       (let [[x y] args
                             value (if (number? x) x (get registers x))]
                         (assoc registers y value))
                       instructions
                       transmission)
          "inc" (recur (inc i)
                       (update registers (first args) inc)
                       instructions
                       transmission)
          "dec" (recur (inc i)
                       (update registers (first args) dec)
                       instructions
                       transmission)
          "jnz" (recur (let [[x y] args
                             value-1 (if (number? x) x (get registers x))
                             value-2 (if (number? y) y (get registers y))]
                         (if (zero? value-1)
                           (inc i)
                           (+ i value-2)))
                       registers
                       instructions
                       transmission)
          "tgl" (recur (inc i)
                       registers
                       (let [[x]   args
                             value (if (number? x) x (get registers x))
                             i     (+ i value)]
                         (if (<= 0 i (dec (count instructions)))
                           (let [[cmd & args] (nth instructions i)
                                 instruction  (case (count args)
                                                1 (if (= "inc" cmd)
                                                    (cons "dec" args)
                                                    (cons "inc" args))
                                                2 (if (= "jnz" cmd)
                                                    (cons "cpy" args)
                                                    (cons "jnz" args)))]
                             (concat (take i instructions)
                                     [instruction]
                                     (drop (inc i) instructions)))))
                       transmission)
          "out" (cond
                  (not= (take (count transmission) expected)
                        transmission)
                  [:failure transmission]

                  (>= (count transmission) sample-size)
                  [:success transmission]

                  :else
                  (let [[x]   args
                        value (if (number? x) x (get registers x))]
                    (recur (inc i)
                           registers
                           instructions
                           (conj transmission value)))))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-25-01-input"))]
    (let [instructions (->> rdr
                            line-seq
                            (mapv prev/parse-instruction))]
      ;; (prn (run-instructions {"a" 7} instructions))
      (loop [input 0]
        (let [result (run-instructions {"a" input} instructions)]
          (prn :input input :result result)
          (match result
            [:success _] (println "SUCCESS with input:" input)
            [:failure _] (recur (inc input))))))))
