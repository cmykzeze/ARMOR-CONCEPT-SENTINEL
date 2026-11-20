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
