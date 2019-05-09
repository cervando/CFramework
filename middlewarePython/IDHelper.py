def getID ( routerName, processName = None ):
    id = (java_string_hashcode( routerName ) << 32) 
    if processName != None:
        id = id | java_string_hashcode(processName)
    return id


def convert_n_bytes( n, b ):
    bits = b*8
    return (n + 2**(bits-1)) % 2**bits - 2**(bits-1)

def convert_4_bytes( n ):
    return convert_n_bytes(n, 4)

def java_string_hashcode( s ):
    h = 0
    n = len(s)
    for i, c in enumerate(s):
        h = h + ord(c)*31**(n-1-i)
    return convert_4_bytes(h)

if __name__ == "__main__":
    print ( 96717 << 32 )
    print ( java_string_hashcode("a") << 32 )
    print ( getID( ("amy")  ) )
    print ( getID( "amy", "wcs"  ) )