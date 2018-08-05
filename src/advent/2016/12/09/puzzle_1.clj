(ns advent.2016.12.09.puzzle-1
  (:require [advent.util.io  :refer (char-seq)]
            [clojure.java.io :as    io]
            [clojure.string  :as    str]))

(defn consume-non-marker-contents
  [{:keys [consuming] :as ctx} c]
  (when (and (not consuming) (not= \( c))
    (-> ctx
        (update :result str c))))

(defn start-consuming-marker
  [{:keys [consuming] :as ctx} c]
  (when (and (not consuming) (= \( c))
    (-> ctx
        (assoc :consuming :marker)
        (assoc :buffer ""))))

(defn continue-consuming-marker
  [{:keys [consuming] :as ctx} c]
  (when (and (= :marker consuming) (not= \) c))
    (-> ctx
        (update :buffer str c))))

(defn finish-consuming-marker
  [{:keys [consuming buffer] :as ctx} c]
  (when (and (= :marker consuming) (= \) c))
    (let [[x y] (->> buffer
                     (re-matches #"(\d+)x(\d+)")
                     rest
                     (map #(Integer/parseInt %)))]
      (-> ctx
          (assoc :consuming :marker-contents
                 :buffer    ""
                 :marker    {:remaining x :repetitions y})))))

(declare consume-char)

(defn consume-marker-contents
  [{:keys [consuming buffer marker result] :as ctx} c]
  (when (= :marker-contents consuming)
    (let [{:keys [remaining repetitions]} marker]
      (if (= 1 remaining)
        (-> ctx
            (update :result str (->> (str buffer c)
                                     (repeat repetitions)
                                     (apply str)))
            (dissoc :consuming :buffer :marker))
        (-> ctx
            (update :buffer str c)
            (update-in [:marker :remaining] dec))))))

(defn consume-char
  [{:keys [marker result] :as ctx} c]
  (or (start-consuming-marker ctx c)
      (continue-consuming-marker ctx c)
      (finish-consuming-marker ctx c)
      (consume-marker-contents ctx c)
      (consume-non-marker-contents ctx c)
      (throw (ex-info "Unhandled case." {:ctx ctx :c c}))))

(defn decompress
  [cs]
  (->> cs
       (reduce consume-char {:result ""})
       :result))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-09-01-input"))]
    (->> rdr char-seq (remove #{\newline}) decompress count prn)))
