(ns advent.2023.12.03.puzzle-1
  (:require [advent.grid     :as grid]
            [clojure.java.io :as io]
            [clojure.string  :as str]))

(def ^:private digit?
  (set "0123456789"))

(defn number-positions
  [grid]
  (apply
    concat
    (map-indexed
      (fn [y line]
        (let [line-length (count line)]
          ;; This is an ugly, imperative mess, but I couldn't think of a more
          ;; functional way to do it.
          (loop [results [], buffer [], x 0]
            (let [character      (nth line x)
                  digit?         (digit? character)
                  buffer'        (if digit?
                                   (conj buffer character)
                                   buffer)
                  end-of-line?   (= x (dec line-length))
                  end-of-number? (or (not digit?) end-of-line?)
                  results'       (if end-of-number?
                                   (concat
                                     results
                                     (when (seq buffer')
                                       [(let [number-string (apply str buffer')]
                                          {:coords
                                           (map
                                             #(vector % y)
                                             (let [x' (if (and
                                                            digit?
                                                            end-of-line?)
                                                        (inc x)
                                                        x)]
                                               (range
                                                 (- x' (count number-string))
                                                 x')))

                                           :number
                                           (parse-long number-string)})]))
                                   results)]
              (cond
                end-of-line?
                results'

                end-of-number?
                (recur results' [] (inc x))

                :else
                (recur results' buffer' (inc x)))))))
      grid)))

(defn part-number?
  [{:keys [coords]} grid]
  (->> coords
       (mapcat #(grid/neighbors-with-diagonal-v2 % grid))
       distinct
       (remove (set coords))
       (map #(grid/value-at-coords grid %))
       (some #(and
                (not (digit? %))
                (not= \. %)))))

(comment
  (def example-input
    (-> "467..114..
         ...*......
         ..35..633.
         ......#...
         617*......
         .....+.58.
         ..592.....
         ......755.
         ...$.*....
         .664.598.."
        (str/replace " " "")))

  (println example-input)

  ;;=> ({:coords ([0 0] [1 0] [2 0]), :number 467}
  ;;    {:coords ([5 0] [6 0] [7 0]), :number 114}
  ;;    ...)
  (number-positions (grid/str->grid example-input))

  (let [grid (grid/str->grid example-input)]
    (reduce
      (fn [sum {:keys [number] :as number-position}]
        (if (part-number? number-position grid)
          (+ sum number)
          sum))
      0
      (number-positions grid))))

(defn -main
  []
  (let [grid (-> (io/resource "2023-12-03-01-input")
                 slurp
                 grid/str->grid)]
    (->> (number-positions grid)
         (filter #(part-number? % grid))
         (reduce
           (fn [sum {:keys [number] :as number-position}]
             (if (part-number? number-position grid)
               (+ sum number)
               sum))
           0)
         prn)))
