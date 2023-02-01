(ns identicon.core
  (:require [digest])
  (:require [clojure.string :as str])
  (import java.io.File)
  (import java.awt.Color)
  (import java.awt.image.BufferedImage)
  (import javax.imageio.ImageIO)
  )
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
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
