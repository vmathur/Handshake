from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json
import time
from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, asynchronous
from tornado import gen

class ConnectionRegisterHandler(RequestHandler):
	def post(self):
		user_id = self.get_argument('user_id')
		devices = self.get_argument('devices')
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		responses = []
		devices = devices.split(',')
		for device in devices:
			print device
			try:
				# response = con.search(term('1', 'bluetooth_address'))
				response = con.search("<bluetooth_address>"+device+"</bluetooth_address>")
				res = response.get_documents().items()[0][1]['profile']	
				print res	
				responses.append(res)
			except pycps.APIError as e:
				print 'none found'

		self.write(json.dumps(responses))
		self.finish()