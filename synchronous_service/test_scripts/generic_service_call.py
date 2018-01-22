#!/bin/env python

"""Provides a Python interface to a hosted webservice."""

import sys
#import suds
#from suds.cache import NoCache
import logging
logging.basicConfig(level=logging.INFO)
logging.getLogger('suds.client').setLevel(logging.INFO)

# Add your deployment URL here
wsdl_urls = {
        'deployed': "https://api.hetcomp.org/cfgtraining_syncservice",
        'local':"http://localhost:8080/cfgtraining_syncservice",
        }

# Add your webmethod definitions here
webmethods = {
        'add': {
            'service': 'Calculator',
            'argnames': ['numberOne', 'numberTwo'],
            }
        }


def get_wsdl_url(service_name, project_identifier):
    """Returns the WSDL URL for the given project and service."""
    return wsdl_urls[project_identifier] + '/' + service_name + '?wsdl'


def call(methodname, service, argnames, argv):
    n_cmdargs_wanted = len(argnames)+3  # one more for the script name, one more for the method name,
    # and one more for the wsdl location

    if len(argv) < n_cmdargs_wanted:
        print("Not enough arguments, use as follows:")
        print("generic_service_call.py %s %s wsdl_location" % (methodname, ' '.join(argnames)))
        print("(Available wsdl-location keys: {})".format(', '.join(wsdl_urls.keys())))
        exit()

    args = argv[2:n_cmdargs_wanted-1]

    wsdlLocation = get_wsdl_url(service, argv[n_cmdargs_wanted-1])

    #client = suds.client.Client(wsdlLocation, cache=NoCache())
    #method = eval('client.service.' + methodname)
    #response = method(*args)
    #return response
    return "Call executed"


def print_all_webmethods():
        for method in sorted(webmethods):
            print("  %-6s| expected arguments: %s" % (method, ', '.join(webmethods[method]['argnames'])))

def main():
    if len(sys.argv) < 2:
        print("Usage:")
        print("generic_service_call.py COMMAND ARGUMENTS... WSDL_LOCATION")
        print("Available wsdl-location keys: {}".format(', '.join(wsdl_urls.keys())))
        print("Available commands:")
        print_all_webmethods()
        exit()

    methodname = sys.argv[1]
    if methodname not in webmethods.keys():
        print("Unknown webmethod '%s', available webmethods are:" % methodname)
        print_all_webmethods()
        exit()

    method = webmethods[methodname]
    response = call(methodname, method['service'], method['argnames'], sys.argv)
    print(response)


if __name__ == '__main__':
    main()
