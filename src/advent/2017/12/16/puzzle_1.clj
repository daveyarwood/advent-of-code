(ns advent.2017.12.16.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse
  [input]
  (for [move (str/split input #",")]
    (case (first move)
      \s [:spin (parse-long (subs move 1))]
      \x (cons :exchange
               (let [[_ a b] (re-matches
                               #"(\d+)/(\d+)"
                               (subs move 1))]
                 (map parse-long [a b])))
      \p (cons :partner
               (let [[_ a b] (re-matches
                               #"([a-z]+)/([a-z]+)"
                               (subs move 1))]
                 (map first [a b]))))))

(comment
  (= (parse "s1,x3/4,pe/b")
     [[:spin 1]
      [:exchange 3 4]
      [:partner \e \b]]))

(defn dance
  [dancers [move & args]]
  (case move
    :spin
    (let [[amount] args]
      (into (subvec dancers (- (count dancers) amount))
            (subvec dancers 0 (- (count dancers) amount))))

    :exchange
    (let [[i j]    args
          a        (nth dancers i)
          b        (nth dancers j)]
      (assoc dancers j a, i b))

    :partner
    (let [[a b]    args
          i        (.indexOf dancers a)
          j        (.indexOf dancers b)]
      (assoc dancers j a, i b))))

(comment
  (dance (vec "abcde") [:spin 1])         ;=> [\e \a \b \c \d]
  (dance (vec "eabcd") [:exchange 3 4])   ;=> [\e \a \b \d \c]
  (dance (vec "eabdc") [:partner \e \b])  ;=> [\b \a \e \d \c]

  ;;=> "baedc"
  (->> (parse "s1,x3/4,pe/b")
       (reduce dance (vec "abcde"))
       (apply str)))

(defn -main
  []
  (->> (io/resource "2017-12-16-01-input")
       slurp
       str/trim
       parse
       (reduce dance (vec "abcdefghijklmnop"))
       (apply str)
       println))
