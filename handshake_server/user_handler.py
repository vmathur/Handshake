from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import tornado.escape
import json

class UserHandler(RequestHandler):
	def get(self, id):
		self.write("user_id is %s" % id)	
