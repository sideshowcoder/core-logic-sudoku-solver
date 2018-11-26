(ns solve-my-sudoku.core
  (:refer-clojure :exclude [==]) ;; core.logic defines :== as well
  (:require [clojure.core.logic :refer :all]))


;; Sudoku rules

;; fill the numbers 1 - 9 exactly once per
;; - row
;; - column
;; - 3x3 sub-square

;; https://dingo.sbs.arizona.edu/~sandiway/sudoku/examples.html

;; data layout:
;; outer vector: 9 elements top left to bottom right 3x3 vectors
;; inner vectors: 9 elements top left to bottom right 3x3 numbers
;; 1 - 9: set numbers
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


(defn column
  "Get column number N from board B as vector of 9 elements."
  [b n])


(defn row
  "Get row number N from board B as vector of 9 elements."
  [b n])

(defn square
  "Get square number N from board B as vector of 9 elements."
  [b n])


;; How to solve:
;;
;; 1) replace each 0 with a fresh variable
;; 2) ensure variables are only assigned to one number per row
;; 3) ensure variables are only assigned to one number per column
;; 4) ensure variables are only assigned to one number per square

(run* [q]
  )
