(ns solve-my-sudoku.core
  (:refer-clojure :exclude [==]) ;; core.logic defines :== as well
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))


;; Sudoku rules

;; fill the numbers 1 - 9 exactly once per
;; - row
;; - column
;; - 3x3 sub-square

;; https://dingo.sbs.arizona.edu/~sandiway/sudoku/examples.html

;; data layout:
;; outer vector: 9 elements top left to bottom right 3x3 vectors
;; inner vectors: 9 elements top left to bottom right 3x3 numbers
;; 1 - 9: set numbersn
;; 0: not filled in number

(def wildcatjan17
  [[0 0 0
    6 8 0
    1 9 0]
   [2 6 0
    0 7 0
    0 0 4]
   [7 0 1
    0 9 0
    5 0 0]
   [8 2 0
    0 0 4
    0 5 0]
   [1 0 0
    6 0 2
    0 0 3]
   [0 4 0
    9 0 0
    0 2 8]
   [0 0 9
    0 4 0
    7 0 3]
   [3 0 0
    0 5 0
    0 1 8]
   [0 7 4
    0 3 6
    0 0 0]])

(defn symbolize-square
  [prefix v]
  (map-indexed #(if (zero? %2) (str prefix %1) %2) v))

(defn symbolize-board
  [board]
  (let [pfxs "abcdefghi"]
    (map-indexed (fn [idx el] (symbolize-square (nth pfxs idx) el)) board)))

(defn board-symbols
  [board]
  (->> board
       symbolize-board
       flatten
       (filter string?)
       (map symbol)))

(defn numbers-in-column
  "Get column number N from board B as vector of 9 elements."
  [b n]
  )

(def row-indexes [[0  1  2  9  10 11 18 19 20]
                  [3  4  5  12 13 14 21 22 23]
                  [6  7  8  15 16 17 24 25 26]
                  [27 28 29 36 37 38 45 46 47]
                  [30 31 32 39 40 41 48 49 50]
                  [33 34 35 42 43 44 51 52 53]
                  [54 55 56 63 64 65 72 73 74]
                  [57 58 59 66 67 68 75 76 77]
                  [60 61 62 69 70 71 78 79 80]])

(def column-indexes [])

(def square-indexes (partition 9 (range 81)))

(defn elements-in-row
  [b n]
  (-> (vec (flatten b))
      (select-keys (nth row-indexes n))
      vals))

(defn elements-in-column
  [b n]
  (-> (vec (flatten b))
      (select-keys (nth column-indexes n))
      vals))

(defn elements-in-square
  [b n]
  (-> (vec (flatten b))
      (select-keys (nth square-indexes n))
      vals))

(defn numbers-in-row
  [b n]
  (->> (elements-in-row b n)
       (filter #(number? %))
       (filter #(not (zero? %)))
       vec))

(defn symbols-in-row
  [b n]
  (->> (elements-in-row b n)
       (filter string?)
       (map symbol)
       vec))

(defn numbers-in-square
  [b n]
  (->> (elements-in-square b n)
       (filter #(number? %))
       (filter #(not (zero? %)))
       vec))

(defn symbols-in-square
  [b n]
  (->> (elements-in-square b n)
       (filter string?)
       (map symbol)
       vec))


(defn numbers-in-square
  "Get square number N from board B as vector of 9 elements."
  [b n]
  (filter #(not (zero? %)) (nth b n)))


;; How to solve:
;;
;; 1) replace each 0 with a fresh variable
;; 2) ensure variables are only assigned to one number per row
;; 3) ensure variables are only assigned to one number per column
;; 4) ensure variables are only assigned to one number per square

(run* [q]
  (fresh [a0 a1 a2 a5 a8
          b2 b3 b5 b6 b7
          c1 c3 c5 c7 c8
          d2 d3 d4 d6 d8
          e1 e2 e4 e6 e7
          f0 f2 f4 f5 f6
          g0 g1 g3 g5 g7
          h1 h2 h3 h5 h6
          i0 i3 i6 i7 i8]
    (== q [a0 a1 a2 a5 a8
           b2 b3 b5 b6 b7
           c1 c3 c5 c7 c8
           d2 d3 d4 d6 d8
           e1 e2 e4 e6 e7
           f0 f2 f4 f5 f6
           g0 g1 g3 g5 g7
           h1 h2 h3 h5 h6
           i0 i3 i6 i7 i8])

    ;; only numbers 1 - 9 are valid
    (fd/in a0 a1 a2 a5 a8
           b2 b3 b5 b6 b7
           c1 c3 c5 c7 c8
           d2 d3 d4 d6 d8
           e1 e2 e4 e6 e7
           f0 f2 f4 f5 f6
           g0 g1 g3 g5 g7
           h1 h2 h3 h5 h6
           i0 i3 i6 i7 i8 (fd/interval 1 9))

    ;; TODO need to flip this around, initializing all fields as lvars, and
    ;; binding them via functions calling out

    ;; each square can use each number only once
    (fd/distinct (concat [a0 a1 a2 a5 a8] (numbers-in-square wildcatjan17 0)))
    (fd/distinct (concat [b2 b3 b5 b6 b7] (numbers-in-square wildcatjan17 1)))
    (fd/distinct (concat [c1 c3 c5 c7 c8] (numbers-in-square wildcatjan17 2)))
    (fd/distinct (concat [d2 d3 d4 d6 d8] (numbers-in-square wildcatjan17 3)))
    (fd/distinct (concat [e1 e2 e4 e6 e7] (numbers-in-square wildcatjan17 4)))
    (fd/distinct (concat [f0 f2 f4 f5 f6] (numbers-in-square wildcatjan17 5)))
    (fd/distinct (concat [g0 g1 g3 g5 g7] (numbers-in-square wildcatjan17 6)))
    (fd/distinct (concat [h1 h2 h3 h5 h6] (numbers-in-square wildcatjan17 7)))
    (fd/distinct (concat [i0 i3 i6 i7 i8] (numbers-in-square wildcatjan17 8)))

    ;; each row can use each number only once
    ;; (let [board (symbolize-board wildcatjan17)]
    ;;   (map #(fd/distinct (concat (symbols-in-row board %) (numbers-in-row board %))) (range 9)))

    ;; each column can use each number only once

    ))
