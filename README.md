#Enigma-Emulator

Complete simulator for the Enigma Machine used during the WW2 by the german forces to encrypt their messages. Although enigma was a physical device with physical rotors and wiring between them, this project aims to simulate the rotors and all of its mechanisms in software, code written in JAVA.

Usage:  java -ea enigma.Main [configuration file] [input file] [output file]

Example config file:

    ABCDEFGHIJKLMNOPQRSTUVWXYZ
    5 3
    I MQ      (AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)
    II ME     (FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)
    III MV    (ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)
    IV MJ     (AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)
    V MZ      (AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)
    VI MZM    (AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)
    VII MZM   (ANOUPFRIMBZTLWKSVEGCJYDHXQ)
    VIII MZM  (AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)
    Beta N    (ALBEVFCYODJWUGNMQTZSKPR) (HIX)
    Gamma N   (AFNIRLBSQWVXGUZDKMTPCOYJHE)
    B R       (AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP)
              (RX) (SZ) (TV)
    C R       (AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) (PW)
              (QZ) (SX) (UY)
First line describes usable alphabet.
Next line describes total number of rotors and moving rotors respectively.
Any number of rotor configurations follow:
  A name containing any non-blank characters other than parentheses.
  One of the characters R, N, or M, indicating that the rotor is a reflector, a non-moving rotor, or a moving rotor, respectively. 
  Immediately after the M for a moving rotor come(s) the letter(s) at which there is a notch on the rotor's ring (no space between M and these letters).
  The cycles of the permutation, using (abc) (cd) format.


Input File:

"*" marks the start of a new input and specifies the rotors used, their orders, as well as plugboard. 
Everything that folows is the message to be encrypted/decrypted.


Example

                Input                               |         Output
    * B Beta III IV I AXLE (HQ) (EX) (IP) (TR) (BY) | QVPQS OKOIL PUBKJ ZPISF XDW
    FROM HIS SHOULDER HIAWATHA                      | BHCNS CXNUO AATZX SRCFY DGU
    TOOK THE CAMERA OF ROSEWOOD                     | FLPNX GXIXT YJUJR CAUGE UNCFM KUF
    MADE OF SLIDING FOLDING ROSEWOOD                | WJFGK CIIRG XODJG VCGPQ OH
    NEATLY PUT IT ALL TOGETHER                      | ALWEB UHTZM OXIIV XUEFP RPR
    IN ITS CASE IT LAY COMPACTLY                    | KCGVP FPYKI KITLB URVGT SFU
    FOLDED INTO NEARLY NOTHING                      | SMBNK FRIIM PDOFJ VTTUG RZM
    BUT HE OPENED OUT THE HINGES                    | UVCYL FDZPG IBXRE WXUEB ZQJO
    PUSHED AND PULLED THE JOINTS                    | YMHIP GRRE
       AND HINGES                                   | GOHET UXDTW LCMMW AVNVJ VH
    TILL IT LOOKED ALL SQUARES                      | OUFAN TQACK
       AND OBLONGS                                  | KTOZZ RDABQ NNVPO IEFQA FS
    LIKE A COMPLICATED FIGURE                       | VVICV UDUER EYNPF FMNBJ VGQ
    IN THE SECOND BOOK OF EUCLID                    |
                                                    | FROMH ISSHO ULDER HIAWA THA
    * B Beta III IV I AXLE (HQ) (EX) (IP) (TR) (BY) | TOOKT HECAM ERAOF ROSEW OOD
    QVPQS OKOIL PUBKJ ZPISF XDW                     | MADEO FSLID INGFO LDING ROSEW OOD
    BHCNS CXNUO AATZX SRCFY DGU                     | NEATL YPUTI TALLT OGETH ER
    FLPNX GXIXT YJUJR CAUGE UNCFM KUF               | INITS CASEI TLAYC OMPAC TLY
    WJFGK CIIRG XODJG VCGPQ OH                      | FOLDE DINTO NEARL YNOTH ING
    ALWEB UHTZM OXIIV XUEFP RPR                     | BUTHE OPENE DOUTT HEHIN GES
    KCGVP FPYKI KITLB URVGT SFU                     | PUSHE DANDP ULLED THEJO INTS
    SMBNK FRIIM PDOFJ VTTUG RZM                     | ANDHI NGES
    UVCYL FDZPG IBXRE WXUEB ZQJO                    | TILLI TLOOK EDALL SQUAR ES
    YMHIP GRRE                                      | ANDOB LONGS
    GOHET UXDTW LCMMW AVNVJ VH                      | LIKEA COMPL ICATE DFIGU RE
    OUFAN TQACK                                     | INTHE SECON DBOOK OFEUC LID
    KTOZZ RDABQ NNVPO IEFQA FS                      |
    VVICV UDUER EYNPF FMNBJ VGQ                     |

