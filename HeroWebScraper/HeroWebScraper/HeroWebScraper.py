from lxml import html
import requests

def height(value):
    if not len(value): return None
    string = value[0]
    feetString = ""
    inchesString = ""
    gettingFeet = True
    for c in string:
        if c == "'":
            gettingFeet = False
        elif not c.isdigit():
            break
        elif gettingFeet:
            feetString += c
        else:
            inchesString += c
    if len(feetString):
        inches = 12 * int(feetString)
        if len(inchesString): inches += int(inchesString)
        return inches
    else: return None

def weight(value):
    if not len(value): return None
    string = value[0]
    pounds = ""
    for c in string:
        if not c.isdigit():
            break
        else:
            pounds += c
    if len(pounds): return int(pounds)
    else: return None

def saveJSON(names, heights, weights):
    if ( not( len(names) == len(heights) == len(weights) )):
        raise ValueError("List sizes are not equal. Names = {0}, Heights = {1}, Weights = {2}.".format(len(names), len(heights), len(weights)))
    fileName = "HeroData.json"
    file = open(fileName,"w")

    rootElement = "Marvel Heroes"
    heroElement = "Hero"
    nameElement = "Name"
    heightElement = "Height"
    weightElement = "Weight"

    fileData = '{"' + rootElement + '": {\n'
    fileData += '\t"' + heroElement + '": [\n'
    for i,name in enumerate(names):
        fileData += '\t\t{"' + nameElement + '": "' + name + '", "' + heightElement + '": "' + str(heights[i]) + '", "' + weightElement + '": "' + str(weights[i]) + '"},\n'
    fileData += '\t]\n}}'

    file.write(fileData)

def saveR(names, heights, weights):
    if ( not( len(names) == len(heights) == len(weights) )):
        raise ValueError("List sizes are not equal. Names = {0}, Heights = {1}, Weights = {2}.".format(len(names), len(heights), len(weights)))
    fileName = "HeroGrapher.R"
    file = open(fileName,"w")

    libraries = "library(car)\n"

    x = "x <- c("
    y = "y <- c("
    labels = "labels <- c("
    for i,name in enumerate(names):
        x += str(weights[i]) + ", "
        y += str(heights[i]) + ", "
        labels += '"' + name + '", '
    x = x[:-2] + ")\n"
    y = y[:-2] + ")\n"
    labels = labels[:-2] + ")\n"

    xLabel = "Weight"
    yLabel = "Height"
    mainLabel = "Marvel Heroes' Height vs Weight"
    numPoints = str(len(names))
    pointColor = "red3"
    plot = 'scatterplot(x, y, xlab = "'+xLabel+'", ylab = "'+yLabel+'", main = "'+mainLabel+'", labels = labels, id.n = '+numPoints+', pch = 20, col = "'+pointColor+'", reg.line = FALSE, boxplots = FALSE)'    
    
    fileData = libraries
    fileData += x
    fileData += y
    fileData += labels
    fileData += plot

    file.write(fileData)





baseURL = "http://marvel.com"
browseHeroesPath = "/characters/browse"
print("{0}".format(baseURL + browseHeroesPath))
heroesWebPage = requests.get(baseURL + browseHeroesPath)
heroesHTMLTree = html.fromstring(heroesWebPage.content)
heroesElements = heroesHTMLTree.xpath('//*[@id="archetype-browse"]/section[2]/div[3]/div/div[2]//a')
heroesNames = list(map(lambda elem : elem.xpath('text()')[0], heroesElements))
heroesPaths = list(map(lambda elem : elem.xpath('@href')[0], heroesElements))
heroesHeights = []
heroesWeights = []

for i,heroPath in enumerate(heroesPaths):
    print("{0} ({1}/{2})".format(baseURL + heroPath, i+1, len(heroesPaths)))
    heroWebPage = requests.get(baseURL + heroPath)
    heroHTMLTree = html.fromstring(heroWebPage.content)
    heroHeight = height(heroHTMLTree.xpath('//*[@id="archetype-detail"]/section[1]/div[2]/div/div/div[2]/div[1]/div[4]/div[3]/p/text()'))
    heroWeight = weight(heroHTMLTree.xpath('//*[@id="archetype-detail"]/section[1]/div[2]/div/div/div[2]/div[1]/div[4]/div[4]/p/text()'))
    heroesHeights.append(heroHeight)
    heroesWeights.append(heroWeight)

i = 0
while i < len(heroesNames):
    if heroesHeights[i] == None or heroesWeights[i] == None:
        del heroesNames[i]
        del heroesHeights[i]
        del heroesWeights[i]
    else:
        i += 1
saveJSON(heroesNames, heroesHeights, heroesWeights)
saveR(heroesNames, heroesHeights, heroesWeights)

