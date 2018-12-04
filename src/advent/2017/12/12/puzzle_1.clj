(ns advent.2017.12.12.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.set     :as set]
            [clojure.string  :as str]))

(defn parse-connections
  [s]
  (let [[_ program connections] (re-matches #"(\d+)\s+<->\s+([0-9, ]+)" s)]
    [(Integer/parseInt program)
     (->> (str/split connections #",\s*")
          (map #(Integer/parseInt %))
          set)]))

(defn program-group
  [centerpoint connections]
  (loop [group #{centerpoint}, seen? #{}, [current & more] [centerpoint]]
    (cond
      (not current)
      group

      (seen? current)
      (recur group seen? more)

      :else
      (recur (set/union group (connections current) #{current})
             (conj seen? current)
             (concat more (connections current))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-12-01-input"))]
    (->> rdr
         line-seq
         (map parse-connections)
         (into {})
         (program-group 0)
         count
         prn)))
