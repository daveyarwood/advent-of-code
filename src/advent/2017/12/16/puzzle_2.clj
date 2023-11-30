(ns advent.2017.12.16.puzzle-2
  (:require [advent.2017.12.16.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.string             :as str]))

(comment
  (let [dance-sequence (p1/parse "s1,x3/4,pe/b")]
    (loop [dancers (vec "abcde"), n 0]
      ;; Scratch code that helped me to realize that there are cycles, so I
      ;; don't need to actually execute all 1,000,000,000 iterations of the
      ;; dance sequence.
      ;;
      ;; In this example, the cycle is 4 iterations long.
      (prn :n n, :dancers (apply str dancers))
      (if (= n 10)
        dancers
        (recur
          (reduce p1/dance dancers dance-sequence)
          (inc n))))))

(defn cycle-length
  [dancers dance-sequence]
  (->> dancers
       (iterate #(reduce p1/dance % dance-sequence))
       rest
       (take-while (partial not= dancers))
       count
       inc))

(defn simulate-running-many-times
  [desired-times dancers dance-sequence]
  (let [cycle-length      (cycle-length dancers dance-sequence)
        simulation-cycles (rem desired-times cycle-length)]
    (loop [dancers dancers, n 0]
      (if (= n simulation-cycles)
        dancers
        (recur
          (reduce p1/dance dancers dance-sequence)
          (inc n))))))

(comment
  ;;=> 4
  (cycle-length (vec "abcde") (p1/parse "s1,x3/4,pe/b"))

  ;;=> [\a \b \c \d \e]
  (simulate-running-many-times
    1000000000
    (vec "abcde")
    (p1/parse "s1,x3/4,pe/b")))

(defn -main
  []
  (->> (io/resource "2017-12-16-01-input")
       slurp
       str/trim
       p1/parse
       (simulate-running-many-times
         1000000000
         (vec "abcdefghijklmnop"))
       (apply str)
       println))
