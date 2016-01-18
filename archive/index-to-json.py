import json

f = open("/Users/happy/archive.d/index/offsite/2007-03-02-0931", 'r')

volumeSet = 'offsite'
volumeName = '2007-03-02-0931'

for l in f:
    r = l.replace('\n', '').split('\t')
    
    fileName = r[0]
    decoder = r[1]
    key = r[2]
    hash = r[3]
    size = r[4]
    
    if decoder != 'plain':
        continue
    
    print json.dumps({
        '_id' : volumeSet + "/" + volumeName + "/" + fileName,
        'key' : key 
    })
