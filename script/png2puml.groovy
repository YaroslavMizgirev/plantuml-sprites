#!/usr/bin/env groovy
@Grab('net.sourceforge.plantuml:plantuml:1.2023.8')

import groovy.cli.commons.CliBuilder
import net.sourceforge.plantuml.klimt.sprite.SpriteGrayLevel
import net.sourceforge.plantuml.klimt.sprite.SpriteUtils

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

final DEFAULT_SCALE = 1
final TMP_DIR = new File('/tmp/pngFile2plantUmlSprite')
TMP_DIR.mkdirs()
final DEFAULT_OUTPUT_DIR = new File('.')

def cli = new CliBuilder(usage: "${this.class.getSimpleName()}.groovy [options] <png file>", stopAtNonOption: false, footer: "Usage example: ./${this.class.getSimpleName()}.groovy logo.png")
cli.o(longOpt: 'output-dir', args: 1, argName: 'output dir', "Output directory for PUML file. Default: current directory")
cli.n(longOpt: 'sprite-name', args: 1, argName: 'sprite name', "Name for the sprite (default: derived from PNG filename)")
cli.s(longOpt: 'scale', args: 1, argName: 'scale', "Scale of sprites. Default value: $DEFAULT_SCALE")

def options = cli.parse(args)
!options && System.exit(1)
if (!options.arguments()) {
    println "error: Missing required PNG file"
    cli.usage()
    System.exit(1)
}
if (options.arguments().size() > 1) {
    println "error: Only one PNG file is supported"
    cli.usage()
    System.exit(1)
}

def pngFile = new File(options.arguments()[0])
if (!pngFile.exists()) {
    println "error: PNG file '${pngFile}' does not exist"
    System.exit(1)
}

println("Processing PNG file: ${pngFile}")

def outputDir = options.o ? new File(options.o) : DEFAULT_OUTPUT_DIR
outputDir.mkdirs()
println("PUML output directory: ${outputDir}")

def spriteName = options.n ?: removeExtension(pngFile.name)
println("Sprite name: ${spriteName}")

def scaleFactor = parseScaleOption(options.s, DEFAULT_SCALE)

static def parseScaleOption(scaleOption, defaultValue) {
    if (!scaleOption) return defaultValue

    try {
        def scale = scaleOption.toDouble()
        if (scale <= 0) {
            println "Error: ${scaleOption} must be positive. Using default: $defaultValue"
            return defaultValue
        }
        return scale
    } catch (NumberFormatException | IllegalArgumentException e) {
        println "Error: ${e.message}. Using default: $defaultValue"
        return defaultValue
    }
}

println("Scale value: ${scaleFactor}")

if (scaleFactor != 1) {
    pngFile = scaleImage(pngFile, scaleFactor, TMP_DIR)
}

def pumlFile = png2PlantUmlSprite(pngFile, outputDir, spriteName)
println("Created PlantUML sprite: ${pumlFile}")

deleteTempFolder(TMP_DIR)

println("Conversion completed successfully!")

static def scaleImage(pngFile, scaleFactor, workDir) {
    BufferedImage im = ImageIO.read(pngFile)
    def width = (im.width * scaleFactor) as Integer
    def height = (im.height * scaleFactor) as Integer
    BufferedImage scaledImage = new BufferedImage(width, height, im.type)
    Graphics2D graphics2D = scaledImage.createGraphics()
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    graphics2D.drawImage(im, 0, 0, width, height, null)
    graphics2D.dispose()
    def fileName = pngFile.name
    def newPngFile = new File(workDir, fileName)
    ImageIO.write(scaledImage, 'png', newPngFile)
    return newPngFile
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

def deleteTempFolder(File folder) {
    if (folder.exists()) {
        folder.eachFile { file ->
            if (file.isDirectory()) {
                deleteTempFolder(file)
            } else {
                file.delete()
            }
        }
        folder.delete()
        println "Temp folder deleted: ${folder.absolutePath}"
    }
}
