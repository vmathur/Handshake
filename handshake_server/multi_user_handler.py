from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class MultiUserHandler(RequestHandler):
	def get(self, id):
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		try:
			res = con.retrieve(id)
		except pycps.APIError as e:
			print(e)
			if e.code == 2824:
				print("Requested non-existing id(s): {0}".format(', '.join(e.document_id)))
			return

		try:
			if res.get_documents:
				print("Found {0} documents: ".format(res.found))
				user = res.get_documents().items()[0]
		except Exception as e:
			print(e)

		connections = user[1]['connections']

		try:
			response = con.lookup(connections, list={'title': 'yes', 'summary': 'no'})
		except pycps.APIError as e:
			print(e)

		self.write(response)