(ns identicon.core
  (:gen-class)
  (:require [digest :refer [md5]])
  (:require [clojure.string :as str])
  (:import java.io.File)
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import javax.imageio.ImageIO)
  )
;;(use 'identicon.digest)
(def tiles-per-side 6)
(def total-tiles (* tiles-per-side tiles-per-side))
(defn- get-color
  [pos]
  (nth '(
    (246 150 121) ;; Pastel Red
    (249 173 129) ;; Pastel Red Orange
    (253 198 137) ;; Pastel Yellow Orange
    (255 247 153) ;; Pastel Yellow
    (196 223 155) ;; Pastel Pea Green
    (163 211 156) ;; Pastel Yellow Green
    (130 202 156) ;; Pastel Green
    (122 204 200) ;; Pastel Green Cyan
    (109 207 246) ;; Pastel Cyan
    (125 167 217) ;; Pastel Cyan Blue
    (131 147 202) ;; Pastel Blue
    (135 129 189) ;; Pastel Blue Violet
    (161 134 190) ;; Pastel Violet
    (189 140 191) ;; Pastel Violet Magenta
    (244 154 193) ;; Pastel Magenta
    (245 152 157) ;; Pastel Magenta Red
         ), pos)
  )
(defn- from-hex-dec
  "Convert a hex char to an int"
  [character]
  (cond
    (= "a" character) 10
    (= "b" character) 11
    (= "c" character) 12
    (= "d" character) 13
    (= "e" character) 14
    (= "f" character) 15
    :else (Integer/parseInt character)
    )
  )
(defn- to-numbers
  "Convert a string of hex chars to a seq of ints"
  [num_string]
  (map from-hex-dec (rest (clojure.string/split num_string #"")))
  )
(defn- to-bools
  "Formulically convert a seq of ints to bools"
  [num_string]
  (take (/ total-tiles 2) (cycle
    (map #(> % 7) (to-numbers num_string))))
  )
(defn- in-row
  "Return the row for a particular position in the seq"
  [pos]
  (quot pos (/ tiles-per-side 2))
  )
(defn- in-col
  "Returns the column for a particular position in the seq"
  [pos]
  (rem pos (/ tiles-per-side 2))
  )
(defn- draw-tile
  "Fill in a tile at a particula position starting from the left of the image"
  [draw tile-size pos]
  (.fillRect draw
    (* (in-col pos) tile-size)
    (* (in-row pos) tile-size)
    tile-size tile-size
  )
  )
(defn- draw-mirror-tile
  "Fill in a tile at a particular position starting from the right of the image"
  [draw tile-size pos]
  (.fillRect draw
             (* (- tiles-per-side (in-col pos) 1) tile-size)
             (* (in-row pos) tile-size)
             tile-size tile-size
             )
  )
(defn- draw-it
  "Draw tiles on the image based on a seq of booleans to determine whether the tile is filled in or not"
  [draw tile-size pos bools]
  (do
    (if (first bools)
      (do
        ;; left half
        (draw-tile draw tile-size pos)
        ;; right half
        (draw-mirror-tile draw tile-size pos)
        )
      )
    )
  )
(defn- file-name
  "Generate a file name"
  [name size]
  (str/join "" [name "." size ".png"])
  )
(defn- fill-background
  "Fill in the background with white"
  [draw size]
  (do
    (.setColor draw (Color/WHITE))
    (.fillRect draw 0 0 size size)
    )
  )
(defn- generate
  "Make a new avatar"
  [identifier size]
  (let
      [tile-size (quot size tiles-per-side)
       md5 (digest/md5 identifier)
       icon (BufferedImage. size size BufferedImage/TYPE_INT_RGB)
       [r g b] (get-color (first (to-numbers md5)))
       color (Color. r g b)
       draw (.createGraphics icon)
       ]
    (do
      (fill-background draw size)
      (.setColor draw color)
      (draw-it draw tile-size 0 (to-bools md5))
      (ImageIO/write icon "png" (File. (file-name md5 size)))
      )
      )
  )
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
