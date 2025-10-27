#!/usr/bin/env groovy
@Grab('net.sourceforge.plantuml:plantuml:1.2023.8')
@Grab('org.apache.xmlgraphics:batik-transcoder:1.16')
@Grab('org.apache.xmlgraphics:batik-codec:1.16')

import groovy.cli.commons.CliBuilder
import net.sourceforge.plantuml.klimt.sprite.SpriteGrayLevel
import net.sourceforge.plantuml.klimt.sprite.SpriteUtils
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

final DEFAULT_OUTPUT_DIR = new File('.')

def cli = new CliBuilder(usage: "${this.class.getSimpleName()}.groovy [options] <svg file>", stopAtNonOption: false, footer: "Usage example: ./${this.class.getSimpleName()}.groovy logo.svg")
cli.o(longOpt: 'output-dir', args: 1, argName: 'output dir', "Output directory for both PNG and PUML files. Default: current directory")
cli.p(longOpt: 'png-dir', args: 1, argName: 'png dir', "Output directory for PNG files (overrides --output-dir for PNG)")
cli.s(longOpt: 'sprite-name', args: 1, argName: 'sprite name', "Name for the sprite (default: derived from SVG filename)")

def options = cli.parse(args)
!options && System.exit(1)
if (!options.arguments()) {
    println "error: Missing required SVG file"
    cli.usage()
    System.exit(1)
}
if (options.arguments().size() > 1) {
    println "error: Only one SVG file is supported"
    cli.usage()
    System.exit(1)
}

def svgFile = new File(options.arguments()[0])
if (!svgFile.exists()) {
    println "error: SVG file '${svgFile}' does not exist"
    System.exit(1)
}

println("Processing SVG file: ${svgFile}")

def outputDir = options.o ? new File(options.o) : DEFAULT_OUTPUT_DIR
def pngDir = options.p ? new File(options.p) : outputDir

outputDir.mkdirs()
pngDir.mkdirs()
println("PNG output directory: ${pngDir}")
println("PUML output directory: ${outputDir}")

def spriteName = options.s ?: removeExtension(svgFile.name)
println("Sprite name: ${spriteName}")

def pngFile = svg2Png(svgFile, pngDir)
println("Created PNG: ${pngFile}")

def pumlFile = png2PlantUmlSprite(pngFile, outputDir, spriteName)
println("Created PlantUML sprite: ${pumlFile}")

println("Conversion completed successfully!")

static def svg2Png(svgFile, outputDir) {
    def fileName = replaceExtension(svgFile.name, "png")
    def pngFile = new File(outputDir, fileName)
    
    // Получаем реальные размеры из SVG
    def dimensions = getSVGRealDimensions(svgFile)
    println "SVG real dimensions: ${dimensions.width}x${dimensions.height}"

    OutputStream pngOut = new FileOutputStream(pngFile)
    try {
        PNGTranscoder transcoder = new PNGTranscoder()
        
        // ЯВНО устанавливаем реальные размеры
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, dimensions.width as float)
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, dimensions.height as float)

        transcoder.transcode(new TranscoderInput(svgFile.toURI().toString()), new TranscoderOutput(pngOut))
    } finally {
        pngOut.close()
    }

    // Проверяем результат
    def bufferedImage = ImageIO.read(pngFile)
    println "PNG result dimensions: ${bufferedImage.width}x${bufferedImage.height}"

    return pngFile
}

static def getSVGRealDimensions(svgFile) {
    def svgText = svgFile.text
    
    // Парсим viewBox - это самый надежный способ получить реальные размеры
    def viewBoxMatch = (svgText =~ /viewBox\s*=\s*"([^"]+)"/)
    def widthMatch = (svgText =~ /width\s*=\s*"([^"]+)"/)
    def heightMatch = (svgText =~ /height\s*=\s*"([^"]+)"/)
    
    float width = 0
    float height = 0
    
    // Приоритет 1: viewBox (самый точный)
    if (viewBoxMatch) {
        def viewBoxParts = viewBoxMatch[0][1].split("\\s+")
        if (viewBoxParts.size() >= 4) {
            width = parseSVGUnit(viewBoxParts[2])
            height = parseSVGUnit(viewBoxParts[3])
            println "Using viewBox dimensions: ${width}x${height}"
        }
    }
    
    // Приоритет 2: явные width/height
    if (width <= 0 && widthMatch) {
        width = parseSVGUnit(widthMatch[0][1])
    }
    if (height <= 0 && heightMatch) {
        height = parseSVGUnit(heightMatch[0][1])
    }
    
    // Приоритет 3: дефолтные значения
    if (width <= 0) {
        width = 100
        println "Using default width: 100"
    }
    if (height <= 0) {
        height = 100
        println "Using default height: 100"
    }
    
    return [width: width, height: height]
}

static def parseSVGUnit(value) {
    if (!value) return 0
    
    // Убираем единицы измерения и преобразуем в число
    def numericValue = value.replaceAll("[^0-9.]", "")
    if (numericValue.isNumber()) {
        return numericValue.toFloat()
    }
    return 0
}

static def replaceExtension(filename, newExtension) {
    int lastDotIndex = filename.lastIndexOf('.')
    if (lastDotIndex > 0) {
        return filename.substring(0, lastDotIndex) + "." + newExtension
    } else {
        return filename + "." + newExtension
    }
}

static def removeExtension(filename) {
    int lastDotIndex = filename.lastIndexOf('.')
    if (lastDotIndex > 0) {
        return filename.substring(0, lastDotIndex)
    } else {
        return filename
    }
}

static def png2PlantUmlSprite(pngFile, outputDir, spriteName) {
    BufferedImage im = ImageIO.read(pngFile)
    removeAlpha(im)
    
    def spriteFile = new File(outputDir, "${spriteName}.puml")
    
    spriteFile.text = "@startuml\n" + SpriteUtils.encode(im, spriteName, SpriteGrayLevel.GRAY_16) + "@enduml\n"
    return spriteFile
}

static def removeAlpha(im) {
    Graphics2D graphics = im.createGraphics()
    try {
        graphics.setComposite(AlphaComposite.DstOver)
        graphics.setPaint(Color.WHITE)
        graphics.fillRect(0, 0, im.getWidth(), im.getHeight())
    } finally {
        graphics.dispose()
    }
}
