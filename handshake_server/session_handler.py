from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class SessionHandler(RequestHandler):
	def get(self):
		self.finish()