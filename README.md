# krpanowrapper
a java application for wrapping krpano native lib tu make using it easier

## single/multible files
You can render a single panoramic image automatically and also a bunch of images as a batch job
For a single job, the command line parameter must be a image that matches the filename pattern
For a batch Job, write the folder as command line argument. All panos are used that matches the  filename pattern  

## preconditions:
The filename of a panoramic image must be in a special format: 

filename = <imgname>[-<info>]*.<imgExt>

imgName = (<firstImage>-<LastImage>-<ImageCount>)

info = description|viewPort

description = {x=<text>}
viewPort = {v=<type>-<nr>x<nr>[(<nr>)]}

type = S|F|C
nr = [-]d+[.d+]
imgExt = psd|psb|jpg|jpeg|tiff


Example:  
(IMG_0833-IMG_0836-4)-{v=S-80.84x53.86(-8.27)}-{x=IMG_0833_IMG_0836-4 (2009-08-04)}.psd

To create a files like this in Autopano Giga, use this FilePattern in Renderer:
(%i-%j-%n)-{d=%p-%f}-{p=%a}

