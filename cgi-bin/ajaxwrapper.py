#!/usr/bin/env python

from __future__ import print_function

import os
import cgi
from subprocess import Popen, PIPE, STDOUT

################################################################################
## IMPORTANT NOTE:
## Uncomment the code for the programming language that you want to use.
## And don't forget to edit the absolute path for your script -- 
## it could be /usr/bin or /usr/local/bin or /opt/local/bin or anything else,
## not to mention the horrendous Windows paths...

# # Java
# SCRIPTDIR = 'javaprolog'
# SCRIPT = ['/usr/bin/java', '-cp', 'json-simple-1.1.1.jar:gnuprologjava-0.2.6.jar:.', 'Shrdlite']

# # SWI Prolog
# SCRIPTDIR = 'javaprolog'
# SCRIPT = ['/usr/local/bin/swipl', '-q', '-g', 'main,halt', '-t', 'halt(1)', '-s', 'shrdlite.pl']

# # Haskell
# SCRIPTDIR = 'haskell'
# SCRIPT = ['/usr/bin/runhaskell', 'Shrdlite.hs']

# # Python
# SCRIPTDIR = 'python'
# SCRIPT = ['/usr/bin/python', 'shrdlite.py']

################################################################################

print('Content-type:text/plain')
print()

try:
    while not os.path.isdir(SCRIPTDIR):
        SCRIPTDIR = os.path.join("..", SCRIPTDIR)

    form = cgi.FieldStorage()
    data = form.getfirst('data')
    script = Popen(SCRIPT, cwd=SCRIPTDIR, stdin=PIPE, stdout=PIPE, stderr=PIPE)
    out, err = script.communicate(data)

    print(out)
    if err:
        raise Exception(err)

except:
    import sys, traceback
    print(traceback.format_exc())
    sys.exit(1)
