import random, os
from PIL import Image
R=random.Random(7)
A='src/main/resources/assets/voidwarden/'
for d in ['textures/item','textures/entity/equipment/humanoid','textures/entity/equipment/humanoid_leggings','textures/entity/cape','models/item','items']:
    os.makedirs(A+d,exist_ok=True)
GRAYS=[(38,36,44),(48,46,56),(58,56,66),(70,68,80),(30,28,36)]
GOLDS=[(255,214,64),(255,236,120),(230,170,30),(255,196,40)]
def gray(): return R.choice(GRAYS)+(255,)
def gold(): return R.choice(GOLDS)+(255,)
# ---------- item icons ----------
M={
'helmet':["................","................","...#..........#.","...##........##.","..###########...".replace("..###########...",".##############."),".################"[:16],".###..####..###.",".###..####..###.",".###........###.",".##g........g##.",".##..........##.","................","................","................","................","................"],
'chestplate':["................","..###......###..",".####......####.",".##############.",".##############.",".###..#gg#..###.",".###..gggg..###.",".###..#gg#..###.","..##..######.##.","..##..######.##.","..##..######.##.","......######....","......#gggg#....","......######....","................","................"],
'leggings':["................","..############..","..############..","..###gggggg###..","..############..","..####....####..","..####....####..","..####....####..","..####....####..","..####....####..","..###g....g###..","..###......###..","..###......###..","..####....####..","................","................"],
'boots':["................","................","................","................","................","..####....####..","..####....####..","..####....####..","..#gg#....#gg#..","..####....####..","..####....####..",".#####....#####.",".#####....#####.",".gggg......gggg.","................","................"],
}
M['helmet']=["................","..#..........#..","..##........##..","..############..",".##############.",".##############.",".###..####..###.",".###..####..###.",".###........###.",".##g........g##.",".##..........##.","................","................","................","................","................"]
def icon(mask,name):
    im=Image.new('RGBA',(16,16),(0,0,0,0)); p=im.load()
    for y in range(16):
        for x in range(16):
            c=mask[y][x]
            if c=='.': continue
            edge=any(not(0<=x+dx<16 and 0<=y+dy<16) or mask[y+dy][x+dx]=='.' for dx,dy in((1,0),(-1,0),(0,1),(0,-1)))
            if c=='g': p[x,y]=gold()
            elif edge: p[x,y]=(22,20,28,255)
            else:
                v=gray()
                if y<8 and x<8: v=tuple(min(255,k+14) for k in v[:3])+(255,)
                p[x,y]=v
    for _ in range(6):
        x,y=R.randrange(3,13),R.randrange(3,13)
        for _ in range(4):
            if p[x,y][3] and p[x,y][:3]!=(22,20,28): p[x,y]=gold()
            x+=R.choice([-1,0,1]); y+=R.choice([0,1]); x=max(0,min(15,x)); y=max(0,min(15,y))
    im.save(A+f'textures/item/{name}.png')
for k,v in M.items(): icon(v,'voidwarden_'+k)
ing=["................","................","................","......######....",".....#gggggg#...","....#gggddgg#...","...#ggddddgg#...","..#gdddddggg#...","..#gggggggg#....","...########.....","................","................","................","................","................","................"]
def ingot(mask,name):
    im=Image.new('RGBA',(16,16),(0,0,0,0)); p=im.load()
    for y in range(16):
        for x in range(16):
            c=mask[y][x]
            if c=='.':continue
            p[x,y]={'#':(22,20,28,255),'g':gold(),'d':gray()}[c]
    im.save(A+f'textures/item/{name}.png')
ingot(ing,'voidwarden_ingot')
nug=["................"]*16
nug=list(nug); 
nug[7]="......####......";nug[8]=".....#gggg#.....";nug[9]=".....#gdgg#.....";nug[10]="......####......"
ingot(nug,'voidwarden_nugget')
# ---------- armor layers 64x32 ----------
def region(p,x0,y0,x1,y1):
    for y in range(y0,y1):
        for x in range(x0,x1):
            v=gray()
            if (x//4+y//4)%2==0: v=tuple(min(255,k+6) for k in v[:3])+(255,)
            p[x,y]=v
def trim(p,x0,y0,x1,y1):
    for x in range(x0,x1): p[x,y0]=gold() if x%2==0 else (150,110,20,255); p[x,y1-1]=(22,20,28,255)
def cracks(p,x0,y0,x1,y1,n):
    for _ in range(n):
        x,y=R.randrange(x0,x1),R.randrange(y0,y1)
        for _ in range(R.randrange(4,9)):
            p[x,y]=gold()
            x+=R.choice([-1,0,1]); y+=R.choice([0,1,1]); x=max(x0,min(x1-1,x)); y=max(y0,min(y1-1,y))
def star(p,cx,cy,r=3):
    for d in range(-r,r+1):
        p[cx+d,cy]=gold(); p[cx,cy+d]=gold()
    p[cx,cy]=(255,250,190,255)
    for dx,dy in((1,1),(-1,-1),(1,-1),(-1,1)): p[cx+dx,cy+dy]=GOLDS[2]+(255,)
def bevel_panel(p,x0,y0,x1,y1,accent=True):
    # Raised dark-metal plate with a lit upper bevel and recessed lower seam.
    for x in range(x0,x1):
        p[x,y0]=(112,108,124,255)
        p[x,y1-1]=(18,16,25,255)
    for y in range(y0,y1):
        p[x0,y]=(24,22,31,255)
        p[x1-1,y]=(24,22,31,255)
    if accent and x1-x0>3 and y1-y0>3:
        for x in range(x0+1,x1-1):
            if x%2==0: p[x,y0+1]=gold()
def rivet(p,x,y):
    p[x,y]=(255,238,145,255)
    if x+1<64: p[x+1,y]=(151,111,31,255)
def layer(path,leg):
    im=Image.new('RGBA',(64,32),(0,0,0,0)); p=im.load()
    for r in [(0,0,32,16),(16,16,40,32),(40,16,56,32),(0,16,16,32)]: region(p,*r)
    cracks(p,0,0,32,16,5); cracks(p,16,16,56,32,12); cracks(p,0,16,16,32,6)
    if not leg:
        # Helmet: layered brow, recessed visor, temple studs and crown engraving.
        bevel_panel(p,9,8,15,15,False)
        for x in range(10,14):
            p[x,11]=(5,5,9,255); p[x,12]=(5,5,9,255)
        p[10,10]=gold(); p[13,10]=gold()
        star(p,12,9,1)
        rivet(p,8,13); rivet(p,15,13)

        # Chest: raised central reactor housing, paired ribs and engraved channels.
        bevel_panel(p,20,19,29,31,True)
        for x in (21,22,26,27):
            p[x,21]=(16,14,23,255)
            p[x,28]=gold()
        star(p,24,25,3)
        for y in range(21,29):
            p[19,y]=gold() if y%2==0 else (100,72,25,255)
            p[29,y]=(20,18,28,255)
        for x in (20,28): rivet(p,x,20); rivet(p,x,30)

        # Segmented pauldrons and forearm bracers.
        for x in range(40,56):
            p[x,16]=gold() if x%2==0 else GOLDS[2]+(255,)
            if x%4==0:
                p[x,18]=(18,16,25,255)
                p[x,19]=gold()
        bevel_panel(p,42,20,54,29,False)
        for x in (43,46,49,52): rivet(p,x,21)
        for x in range(2,14):
            p[x,27]=gold() if x%3==0 else (23,21,30,255)
        for x in range(0,16):
            p[x,31]=gold() if x%2==0 else (150,110,20,255)
        for x in range(4,12): p[x,28]=gold()
    else:
        # Layered thigh guards with central grooves and reinforced knee plates.
        bevel_panel(p,19,18,29,25,True)
        bevel_panel(p,20,25,28,31,False)
        for x in range(16,40):
            p[x,16]=gold() if x%2==0 else GOLDS[2]+(255,)
        for x in range(21,28):
            p[x,22]=(14,13,20,255)
        star(p,24,27,2)
        for x in range(0,16):
            p[x,22]=gold() if x%2==0 else GOLDS[2]+(255,)
        for x in (18,29,42,53): rivet(p,x,20)
        # Fine parallel inlays make the greaves read as separate armor plates.
        for y in (26,28,30):
            for x in range(42,54):
                if x%3==0: p[x,y]=gold()
    im.save(path)
layer(A+'textures/entity/equipment/humanoid/voidwarden.png',False)
layer(A+'textures/entity/equipment/humanoid_leggings/voidwarden.png',True)
# ---------- cape texture: 4 segments, each box 7w x 6h x 1d (net 16x7) at v=i*8 ----------
W,H,SEG=7,6,4
master=Image.new('RGBA',(W,H*SEG),(0,0,0,0)); mp=master.load()
for y in range(H*SEG):
    for x in range(W):
        t=y/(H*SEG)
        base=gray()
        # fade to scorched gold toward the hem
        if R.random()<t*0.55: base=gold() if R.random()<0.5 else (110,80,20,255)
        mp[x,y]=base
for y in range(H*SEG):
    if y%5==2:
        for x in range(W):
            if R.random()<0.5: mp[x,y]=gold()
# Tailored panel seams, a central void-sigil and restrained gold piping.
for y in range(H*SEG):
    mp[0,y]=(24,22,31,255)
    mp[W-1,y]=(170,125,34,255) if y%3 else (255,224,105,255)
    if y%6==0:
        for x in range(1,W-1): mp[x,y]=(18,16,25,255)
    elif y%6==1:
        mp[3,y]=(255,236,135,255)
for i in range(SEG):
    cy=i*H+3
    mp[3,cy]=(255,250,190,255)
    mp[2,cy]=(255,205,62,255); mp[4,cy]=(255,205,62,255)
    mp[3,cy-1]=(230,170,30,255); mp[3,cy+1]=(230,170,30,255)
# ragged hem (alpha cut)
for x in range(W):
    cut=R.choice([0,1,2,3,4])
    for y in range(H*SEG-cut,H*SEG): mp[x,y]=(0,0,0,0)
for y in range(H*SEG-9,H*SEG):  # diagonal tatter on the outer edge
    k=(y-(H*SEG-9))//2
    for x in range(W-k,W): mp[x,y]=(0,0,0,0)
cape=Image.new('RGBA',(64,64),(0,0,0,0)); cp=cape.load()
for i in range(SEG):
    oy=i*8
    for y in range(H):
        for x in range(W):
            c=mp[x,i*H+y]
            cp[1+x,1+oy+y]=c            # front face (u=d..d+w, v=d..d+h)
            cp[1+W+1+x,1+oy+y]=c        # back face
        cp[0,1+oy+y]=(30,28,36,255); cp[W+1,1+oy+y]=(30,28,36,255)  # sides
cape.save(A+'textures/entity/cape/voidwarden_cape.png')
print('ok')


# ---------------------------------------------------------------------------
# Deliberate pixel-art pass (takes precedence over the legacy prototype above)
# ---------------------------------------------------------------------------
from PIL import Image, ImageDraw
from pathlib import Path

ART = Path("src/main/resources/assets/voidwarden/textures")
P = {
    "void":(7,8,13,255), "edge":(17,18,25,255), "shadow":(27,29,39,255),
    "base":(39,42,54,255), "mid":(53,57,70,255), "light":(72,77,91,255),
    "steel":(105,111,126,255), "glint":(145,151,164,255),
    "goldshadow":(105,70,18,255), "golddark":(166,116,28,255),
    "gold":(218,164,48,255), "goldlight":(255,215,105,255),
    "goldglint":(255,239,173,255), "reactordark":(36,23,16,255),
    "reactor":(174,101,25,255), "reactorlight":(255,191,68,255)
}
def box(d,b,c): d.rectangle(b,fill=c)
def panel(d,b,accent=True,fill=None):
    x0,y0,x1,y1=b; box(d,b,P["edge"])
    if x1-x0<3 or y1-y0<3: return
    box(d,(x0+1,y0+1,x1-1,y1-1),fill or P["mid"])
    d.line((x0+1,y0+1,x1-1,y0+1),fill=P["steel"])
    d.line((x0+1,y0+1,x0+1,y1-1),fill=P["light"])
    d.line((x0+1,y1-1,x1-1,y1-1),fill=P["void"])
    d.point((x0+1,y0+1),fill=P["glint"])
    if accent and x1-x0>=5:
        d.line((x0+2,y0+2,x1-2,y0+2),fill=P["golddark"])
def rivet(d,x,y):
    d.point((x,y),fill=P["goldglint"])
    if x<63: d.point((x+1,y),fill=P["goldshadow"])
def atlas(regions):
    im=Image.new("RGBA",(64,32),(0,0,0,0)); d=ImageDraw.Draw(im)
    for x0,y0,x1,y1 in regions:
        box(d,(x0,y0,x1-1,y1-1),P["base"])
        for y in range(y0,y1):
            d.line((x0,y,x1-1,y),fill=P["mid"] if (y-y0)%5==0 else P["base"])
        d.line((x0,y0,x1-1,y0),fill=P["steel"])
        d.line((x0,y1-1,x1-1,y1-1),fill=P["void"])
    return im,d

def authored_body():
    im,d=atlas([(0,0,32,16),(16,16,40,32),(40,16,56,32),(0,16,16,32)])
    # Crown and cheek shells; front visor is the head's 8x8 front UV.
    panel(d,(9,1,15,7),True,P["shadow"])
    d.line((11,2,12,1,13,2),fill=P["gold"])
    d.line((12,2,12,5),fill=P["steel"])
    for x in (2,5,18,21):
        panel(d,(x,9,x+3,14),False,P["shadow"])
        d.point((x+1,10),fill=P["steel"])
    panel(d,(8,8,16,16),True,P["mid"])
    box(d,(9,10,14,12),P["void"])
    d.line((10,10,13,10),fill=P["goldlight"])
    d.point((9,11),fill=P["golddark"]); d.point((14,11),fill=P["golddark"])
    d.line((9,13,10,14,13,14,14,13),fill=P["steel"])
    rivet(d,8,14); rivet(d,15,14)
    # Breastplate, layered pectorals and a clearly readable reactor.
    panel(d,(20,16,28,28),False,P["shadow"])
    panel(d,(20,17,23,22),True,P["mid"])
    panel(d,(25,17,28,22),True,P["mid"])
    d.line((21,18,22,19,22,21),fill=P["golddark"])
    d.line((27,18,26,19,26,21),fill=P["golddark"])
    panel(d,(22,20,26,26),True,P["reactordark"])
    box(d,(23,21,24,24),P["reactor"])
    d.point((23,21),fill=P["reactorlight"]); d.point((24,22),fill=P["goldglint"])
    d.line((22,26,25,26),fill=P["golddark"])
    for y in (23,25,27):
        d.line((17,y,19,y),fill=P["steel"]); d.line((29,y,31,y),fill=P["steel"])
    for x in (18,30):
        d.line((x,18,x,21),fill=P["golddark"]); rivet(d,x,17)
    # Rear spine and vent slots.
    panel(d,(32,16,40,28),False,P["shadow"])
    d.line((35,17,35,26),fill=P["steel"]); d.line((36,17,36,26),fill=P["void"])
    for y in (19,21,23):
        d.line((33,y,34,y),fill=P["golddark"]); d.line((37,y,38,y),fill=P["golddark"])
    panel(d,(34,27,38,31),False,P["mid"])
    # Paired shoulder shells and forearm guards.
    for x in (0,40):
        panel(d,(x+1,17,x+7,23),True,P["shadow"])
        d.line((x+2,19,x+5,19),fill=P["steel"])
        d.line((x+3,20,x+5,21),fill=P["golddark"])
        panel(d,(x+2,24,x+7,30),False,P["mid"])
        d.line((x+3,25,x+6,25),fill=P["gold"])
        d.line((x+3,28,x+6,28),fill=P["steel"])
        d.line((x+3,26,x+4,27,x+5,26),fill=P["golddark"])
        rivet(d,x+2,24); rivet(d,x+7,29)
    return im

def authored_legs():
    im,d=atlas([(16,16,40,32),(0,16,16,32),(40,16,56,32)])
    panel(d,(20,16,28,21),True,P["shadow"])
    box(d,(23,17,24,19),P["gold"]); d.point((23,17),fill=P["goldglint"])
    panel(d,(32,16,40,21),False,P["shadow"])
    d.line((35,17,36,17),fill=P["steel"])
    for x in (16,28):
        panel(d,(x,18,x+4,26),False,P["mid"])
        d.line((x+1,20,x+2,22),fill=P["golddark"])
        d.line((x+1,24,x+2,25),fill=P["steel"])
    for x in (0,40):
        panel(d,(x+4,20,x+8,25),True,P["shadow"])
        d.line((x+5,22,x+6,23),fill=P["gold"])
        panel(d,(x+3,25,x+9,29),True,P["mid"])
        d.line((x+5,26,x+6,27),fill=P["goldlight"])
        panel(d,(x+4,29,x+8,32),False,P["shadow"])
        d.line((x+5,30,x+5,31),fill=P["steel"])
        d.line((x+6,30,x+6,31),fill=P["golddark"])
        rivet(d,x+3,28); rivet(d,x+8,28)
        for sx in (x,x+11): d.line((sx,21,sx,31),fill=P["goldshadow"])
    panel(d,(32,21,40,31),False,P["shadow"])
    d.line((35,22,35,29),fill=P["steel"]); d.line((36,22,36,29),fill=P["golddark"])
    return im

def authored_cape():
    im=Image.new("RGBA",(64,64),(0,0,0,0)); d=ImageDraw.Draw(im)
    for seg in range(4):
        oy=seg*8
        for fx in (1,9):
            for yy in range(6):
                for xx in range(7):
                    col=P["shadow"] if xx in (0,6) or yy in (0,5) else P["base"]
                    if xx==3 and yy not in (0,5): col=P["mid"]
                    d.point((fx+xx,oy+1+yy),fill=col)
            d.line((fx,oy+1,fx,oy+6),fill=P["golddark"])
            d.line((fx+6,oy+1,fx+6,oy+6),fill=P["golddark"])
            d.line((fx+1,oy+6,fx+5,oy+6),fill=P["edge"])
            cx,cy=fx+3,oy+3
            d.point((cx,cy-1),fill=P["golddark"])
            d.point((cx-1,cy),fill=P["gold"])
            d.point((cx,cy),fill=P["goldglint"])
            d.point((cx+1,cy),fill=P["gold"])
            d.point((cx,cy+1),fill=P["golddark"])
            d.point((cx,oy+1),fill=P["steel"])
            d.point((cx,oy+6),fill=P["goldshadow"])
        for yy in range(1,7):
            d.point((0,oy+yy),fill=P["golddark"] if yy in (1,6) else P["edge"])
            d.point((8,oy+yy),fill=P["golddark"] if yy in (1,6) else P["edge"])
    return im

def authored_icon(kind):
    im=Image.new("RGBA",(32,32),(0,0,0,0)); d=ImageDraw.Draw(im)
    def r(b,c): d.rectangle(b,fill=c,outline=P["void"])
    if kind=="helmet":
        r((6,6,25,25),P["shadow"]); r((8,4,23,10),P["mid"])
        d.line((9,5,22,5),fill=P["glint"]); r((8,10,23,22),P["base"])
        r((9,13,22,17),P["void"]); d.line((11,13,20,13),fill=P["goldlight"])
        d.line((8,19,11,23,20,23,23,19),fill=P["steel"])
        d.line((15,6,15,10),fill=P["gold"])
        r((5,12,7,20),P["mid"]); r((24,12,26,20),P["mid"])
        d.point((7,20),fill=P["gold"]); d.point((24,20),fill=P["gold"])
    elif kind=="chestplate":
        r((4,7,11,14),P["mid"]); r((20,7,27,14),P["mid"])
        r((8,8,23,27),P["shadow"]); r((10,10,21,25),P["base"])
        d.line((10,10,21,10),fill=P["glint"])
        d.line((7,12,9,17,11,18),fill=P["gold"])
        d.line((24,12,22,17,20,18),fill=P["gold"])
        r((13,14,18,22),P["reactordark"]); r((14,15,17,21),P["reactor"])
        d.point((14,15),fill=P["reactorlight"]); d.line((12,25,19,25),fill=P["golddark"])
    elif kind=="leggings":
        r((6,5,25,11),P["shadow"]); d.line((8,6,23,6),fill=P["gold"])
        r((7,10,14,27),P["mid"]); r((17,10,24,27),P["mid"])
        d.line((8,12,8,25),fill=P["steel"]); d.line((23,12,23,25),fill=P["steel"])
        r((8,15,13,19),P["shadow"]); r((18,15,23,19),P["shadow"])
        d.point((10,16),fill=P["goldlight"]); d.point((20,16),fill=P["goldlight"])
        d.line((10,21,10,25),fill=P["golddark"]); d.line((21,21,21,25),fill=P["golddark"])
    elif kind=="boots":
        for x in (4,17):
            r((x+2,8,x+9,23),P["mid"]); r((x,20,x+12,27),P["shadow"])
            d.line((x+3,10,x+8,10),fill=P["steel"])
            d.line((x+3,14,x+8,14),fill=P["golddark"])
            d.line((x+1,25,x+11,25),fill=P["gold"])
            d.point((x+2,21),fill=P["goldlight"])
    return im

def final_art_pass():
    (ART/"entity/equipment/humanoid").mkdir(parents=True,exist_ok=True)
    (ART/"entity/equipment/humanoid_leggings").mkdir(parents=True,exist_ok=True)
    (ART/"entity/cape").mkdir(parents=True,exist_ok=True)
    (ART/"item").mkdir(parents=True,exist_ok=True)
    authored_body().save(ART/"entity/equipment/humanoid/voidwarden.png",optimize=True)
    authored_legs().save(ART/"entity/equipment/humanoid_leggings/voidwarden.png",optimize=True)
    authored_cape().save(ART/"entity/cape/voidwarden_cape.png",optimize=True)
    for name in ("helmet","chestplate","leggings","boots"):
        authored_icon(name).save(ART/f"item/voidwarden_{name}.png",optimize=True)
    for name,size in (("ingot",32),("nugget",16)):
        im=Image.new("RGBA",(size,size),(0,0,0,0)); d=ImageDraw.Draw(im)
        if name=="ingot":
            d.polygon([(7,8),(22,4),(27,20),(11,27),(4,17)],fill=P["golddark"],outline=P["void"])
            d.polygon([(8,9),(21,6),(24,18),(11,23),(7,16)],fill=P["gold"])
            d.line((9,10,20,7,22,17),fill=P["goldglint"])
            d.line((11,22,24,18),fill=P["goldshadow"])
            d.line((12,12,17,10,19,15),fill=P["shadow"])
        else:
            d.polygon([(4,8),(10,5),(13,10),(9,13),(3,11)],fill=P["gold"],outline=P["void"])
            d.line((5,8,9,6,11,9),fill=P["goldglint"])
            d.point((7,10),fill=P["goldshadow"])
        im.save(ART/f"item/voidwarden_{name}.png",optimize=True)

if __name__ == "__main__":
    final_art_pass()
